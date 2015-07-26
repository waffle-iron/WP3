package eu.modelwriter.marker.internal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import eu.modelwriter.marker.Activator;
import eu.modelwriter.marker.xml.EventMemento;
import eu.modelwriter.marker.xml.XMLDOMHelper;

public class MarkerFactory {

  // Marker ID
  public static final String MARKER = "eu.modelwriter.writer.markers.marker";

  // Annotation ID
  public static final String ANNOTATION = "eu.modelwriter.writer.markers.annotation";

  public static final String GROUP_ID = "GROUP_ID";
  public static final String LEADER_ID = "LEADER_ID";

  public static IMarker createMarker(IResource resource, ITextSelection selection)
      throws CoreException {

    if (selection != null && !selection.getText().isEmpty()) {

      int start = selection.getOffset();
      int end = selection.getOffset() + selection.getLength();

      HashMap<String, Object> map = new HashMap<String, Object>();

      MarkerUtilities.setLineNumber(map, selection.getStartLine());
      MarkerUtilities.setMessage(map, selection.getText());
      MarkerUtilities.setCharStart(map, start);
      MarkerUtilities.setCharEnd(map, end);
      map.put(IMarker.TEXT, selection.getText());
      map.put(IMarker.LOCATION, selection.getStartLine());
      map.put(IMarker.SOURCE_ID, UUID.randomUUID().toString());
      MarkerUtilities.createMarker(resource, map, MARKER);

    } else {
      MessageDialog dialog = new MessageDialog(Activator.getShell(), "Mark Information", null,
          "Please perform a valid selection", MessageDialog.WARNING, new String[] {"OK"}, 0);
      dialog.open();
    }
    return findMarkerByOffset(resource, selection.getOffset());
  }

  /*
   * Creates a Marker
   */
  public static IMarker createMarker(IResource res, ITreeSelection selection) throws CoreException {

    IMarker marker = null;
    if (selection != null && Activator.getEditor() instanceof EcoreEditor
        && selection.getFirstElement() instanceof ENamedElement
        && ((ENamedElement) selection.getFirstElement()).getName() != null
        && !((ENamedElement) selection.getFirstElement()).getName().isEmpty()) {

      ENamedElement element = (ENamedElement) selection.getFirstElement();
      String selectedText = element.getName();

      URI uri = EcoreUtil.getURI(element);
      System.out.println(uri);
      String xpath = XMLDOMHelper.findNodeAndGetXPath(selectedText,
          res.getLocation().toFile().getAbsolutePath());
      System.out.println(xpath);

      XMLInputFactory factory = XMLInputFactory.newInstance();
      try {
        XMLStreamReader streamReader =
            factory.createXMLStreamReader(new FileReader(res.getLocation().toFile()));

        EventMemento memento = null;
        EventMemento current = null;
        String elementName = null;
        while (streamReader.hasNext()) {
          // XMLStreamHelper.printEvent(streamReader, true);
          if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
            String name = streamReader.getAttributeValue(null, "name");
            if (name != null && name.equals(selectedText)) {
              elementName = streamReader.getName().toString();
              break;
            }
          }
          memento = new EventMemento(streamReader);
          streamReader.next();
          current = new EventMemento(streamReader);
        }
        streamReader.close();

        // Fetch IResource
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
        IFile file = input.getFile();

        // JFace Text Document object is created to get character offsets from line numbers.
        String charsetName = streamReader.getCharacterEncodingScheme();
        if (charsetName == null)
          charsetName = "UTF-8";
        Scanner scanner = new Scanner(file.getContents(), charsetName);
        IDocument document = new Document(scanner.useDelimiter("\\A").next());
        scanner.close();

        int start = 0;
        System.out.println("Previous Line Number" + (memento.getLineNumber() - 1));
        int end = 0;
        System.out.println("Current Line Number" + current.getLineNumber());
        try {
          IRegion startRegion = document.getLineInformation(memento.getLineNumber() - 1);
          start = startRegion.getOffset() + memento.getColumnNumber() - 2;
          IRegion endRegion = document.getLineInformation(current.getLineNumber());
          end = endRegion.getOffset() - 1;
        } catch (BadLocationException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        // System.out.println(start);
        // System.out.println(end);
        int length = end - start;

        // Create Marker
        HashMap<String, Object> map = new HashMap<String, Object>();
        MarkerUtilities.setLineNumber(map, current.getLineNumber());
        MarkerUtilities.setMessage(map, selectedText);
        MarkerUtilities.setCharStart(map, start);
        MarkerUtilities.setCharEnd(map, end);
        map.put(IMarker.TEXT, elementName);
        map.put(IMarker.LOCATION, current.getLineNumber());
        map.put(IMarker.SOURCE_ID, UUID.randomUUID().toString());
        map.put("uri", uri.toString());
        map.put("xpath", xpath);
        marker = file.createMarker(MARKER);
        if (marker.exists()) {
          try {
            marker.setAttributes(map);
          } catch (CoreException e) {
            // You need to handle the case where the marker no longer exists
            e.printStackTrace();
          }
        }

        // Create Annotation Model
        ResourceMarkerAnnotationModel rmam = new ResourceMarkerAnnotationModel(file);
        SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(ANNOTATION, marker);
        rmam.addAnnotation(ma, new Position(start, length));

        // Refresh the model of the TreeViewer
        EcoreEditor ecoreEditor = (EcoreEditor) Activator.getEditor();
        ecoreEditor.getViewer().refresh();

      } catch (XMLStreamException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      MessageDialog dialog = new MessageDialog(Activator.getShell(), "Mark Information", null,
          "Please perform a valid selection", MessageDialog.WARNING, new String[] {"OK"}, 0);
      dialog.open();
    }
    return marker;
  }

  /*
   * returns a list of a resources markers
   */
  public static List<IMarker> findMarkers(IResource resource) {
    try {
      List<IMarker> myMarkerList =
          Arrays.asList(resource.findMarkers(MARKER, true, IResource.DEPTH_ZERO));
      return myMarkerList;
    } catch (CoreException e) {
      return new ArrayList<IMarker>();
    }
  }

  /*
   * returns a list of a resources markers
   */
  public static ArrayList<IMarker> findMarkersAsArrayList(IResource resource) throws CoreException {

    ArrayList<IMarker> myMarkerList = new ArrayList<IMarker>();
    IMarker[] list = resource.findMarkers(MARKER, true, IResource.DEPTH_ZERO);
    for (IMarker iMarker : list) {
      myMarkerList.add(iMarker);
    }
    return myMarkerList;

  }


  public static IMarker findMarkerByOffset(IResource resource, int offset) {
    IMarker marker = null;
    try {
      List<IMarker> mList = findAllMarkers(resource);
      int start;
      int end;
      for (IMarker iMarker : mList) {
        start = (int) iMarker.getAttribute(IMarker.CHAR_START);
        end = (int) iMarker.getAttribute(IMarker.CHAR_END);
        if (offset <= end && offset >= start) {
          return iMarker;
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return marker;
  }

  public static IMarker findMarkerBySourceId(IResource resource, String id) {
    IMarker marker = null;
    try {
      List<IMarker> mList = findAllMarkers(resource);
      for (IMarker iMarker : mList) {
        if (id.equals(iMarker.getAttribute(IMarker.SOURCE_ID))) {
          return iMarker;
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return marker;
  }

  public static List<IMarker> findMarkersByGroupId(IResource resource, String groupId)
      throws CoreException {
    List<IMarker> groupMarkers = new ArrayList<IMarker>();
    List<IMarker> markerList = findAllMarkers(resource);
    for (IMarker iMarker : markerList) {
      if (groupId.equals(iMarker.getAttribute(GROUP_ID))) {
        groupMarkers.add(iMarker);
      }
    }
    return groupMarkers;
  }

  public static IMarker findMarkerByXpath(IResource resource, String xpath) {
    IMarker marker = null;
    if (xpath == null || xpath.isEmpty())
      return null;
    try {
      List<IMarker> mList = findAllMarkers(resource);
      for (IMarker iMarker : mList) {
        if (xpath.equals(iMarker.getAttribute("xpath"))) {
          return iMarker;
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return marker;
  }

  public static String getQualifiedName(ITreeSelection selections) {
    TreePath[] paths = selections.getPaths();

    // Consider only not empty and single selection
    if (selections.isEmpty() || selections.size() > 1)
      return null;

    TreePath path = paths[0];
    IElementComparer comparer = null;
    if (selections instanceof TreeSelection) {
      comparer = ((TreeSelection) selections).getElementComparer();
    }
    System.out.println(path.hashCode(comparer));
    for (int i = 1; i < path.getSegmentCount(); i++) {
      if (path.getSegment(i) instanceof ResourceFactoryImpl) {
        EcoreResourceFactoryImpl eResourceFactory = (EcoreResourceFactoryImpl) path.getSegment(i);
        System.out
            .println(eResourceFactory.getClass().getName() + ": " + eResourceFactory.toString());
      } else if (path.getSegment(i) instanceof ENamedElement) {
        ENamedElement namedElement = (ENamedElement) path.getSegment(i);
        System.out.println(namedElement.getClass().getName() + ": " + namedElement.getName());
      } else {
        System.out.println("?");
      }
    }
    return null;
  }

  public static IDocument getDocument() {
    MultiPageEditorPart mpepEditor;
    ITextEditor iteEditor;
    if (Activator.getEditor() instanceof MultiPageEditorPart) {
      mpepEditor = (MultiPageEditorPart) Activator.getEditor();
      IEditorPart[] editors = mpepEditor.findEditors(mpepEditor.getEditorInput());
      iteEditor = (ITextEditor) editors[0];
    } else
      iteEditor = (ITextEditor) Activator.getEditor();
    IDocumentProvider idp = iteEditor.getDocumentProvider();
    return idp.getDocument(iteEditor.getEditorInput());

  }

  public static String getCurrentEditorContent() {
    return getDocument().get();
  }

  /*
   * Returns a list of markers that are linked to the resource or any sub resource of the resource
   */
  public static List<IMarker> findAllMarkers(IResource resource) {
    try {
      return Arrays.asList(resource.findMarkers(MARKER, true, IResource.DEPTH_INFINITE));
    } catch (CoreException e) {
      return new ArrayList<IMarker>();
    }
  }

  /*
   * Returns the selection of the package explorer
   */
  public static TreeSelection getTreeSelection() {

    ISelection selection =
        Activator.getActiveWorkbenchWindow().getSelectionService().getSelection();
    if (selection instanceof TreeSelection) {
      return (TreeSelection) selection;
    }
    return null;
  }

  /*
   * Returns the selection of the package explorer
   */
  public static TextSelection getTextSelection() {

    ISelection selection =
        Activator.getActiveWorkbenchWindow().getSelectionService().getSelection();
    if (selection instanceof TextSelection) {
      return (TextSelection) selection;
    }
    return null;
  }

  public static ISelection getSelection() {
    ISelection selection =
        Activator.getActiveWorkbenchWindow().getSelectionService().getSelection();
    return selection;
  }

  public static void addAnnotation(IMarker marker, ITextSelection selection, IEditorPart editor) {
    // The DocumentProvider enables to get the document currently loaded in
    // the editor
    MultiPageEditorPart mpepEditor;
    ITextEditor iteEditor;
    if (editor instanceof MultiPageEditorPart) {
      mpepEditor = (MultiPageEditorPart) editor;
      IEditorPart[] editors = mpepEditor.findEditors(mpepEditor.getEditorInput());
      iteEditor = (ITextEditor) editors[0];
    } else
      iteEditor = (ITextEditor) editor;

    IDocumentProvider idp = iteEditor.getDocumentProvider();

    // This is the document we want to connect to. This is taken from the
    // current editor input.
    IDocument document = idp.getDocument(iteEditor.getEditorInput());

    // The IannotationModel enables to add/remove/change annoatation to a
    // Document loaded in an Editor
    IAnnotationModel iamf = idp.getAnnotationModel(iteEditor.getEditorInput());

    // Note: The annotation type id specify that you want to create one of
    // your annotations
    SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(ANNOTATION, marker);

    // Finally add the new annotation to the model
    iamf.connect(document);
    iamf.addAnnotation(ma, new Position(selection.getOffset(), selection.getLength()));
    iamf.disconnect(document);
  }
}

