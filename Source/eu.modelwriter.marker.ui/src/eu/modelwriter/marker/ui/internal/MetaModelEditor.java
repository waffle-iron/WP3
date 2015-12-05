package eu.modelwriter.marker.ui.internal;

import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Util;
import edu.mit.csail.sdg.alloy4graph.GraphViewer;
import edu.mit.csail.sdg.alloy4viz.AlloyAtom;
import edu.mit.csail.sdg.alloy4viz.AlloyInstance;
import edu.mit.csail.sdg.alloy4viz.MagicColor;
import edu.mit.csail.sdg.alloy4viz.MagicLayout;
import edu.mit.csail.sdg.alloy4viz.StaticInstanceReader;
import edu.mit.csail.sdg.alloy4viz.VizGraphPanel;
import edu.mit.csail.sdg.alloy4viz.VizState;
import eu.modelwriter.configuration.alloy.AlloyParserForMetamodel;
import eu.modelwriter.configuration.internal.AlloyUtilities;
import eu.modelwriter.marker.internal.MarkUtilities;
import eu.modelwriter.marker.internal.MarkerFactory;
import eu.modelwriter.marker.ui.internal.views.visualizationview.Visualization;

public class MetaModelEditor extends MultiPageEditorPart {
  public static final String ID = "eu.modelwriter.marker.ui.views.metamodelview";
  private static VizState myState = null;
  private static VizGraphPanel graph;
  private static Frame frame;
  private static File file = null;
  public static Object rightClickedAnnotation;
  static String xmlFileName = null;
  static Composite modelEditor;
  private TextEditor textEditor;

  private void addDropListener() {
    final int acceptableOps = DnDConstants.ACTION_COPY;
    @SuppressWarnings("unused")
    final DropTarget dropTarget = new DropTarget(MetaModelEditor.graph.alloyGetViewer(),
        acceptableOps, new DropTargetListener() {
          ISelection selection;
          IFile file;

          private void createMarker(final String type) {
            Display.getDefault().asyncExec(new Runnable() {

              @Override
              public void run() {
                file = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor().getEditorInput().getAdapter(IFile.class);
                selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getSelectionService().getSelection();
                if (selection instanceof ITextSelection) {
                  IMarker marker;
                  final ITextSelection textSelection = (ITextSelection) selection;
                  final int start = textSelection.getOffset();
                  final int end = textSelection.getOffset() + textSelection.getLength();

                  marker = MarkerFactory.findMarkerWithAbsolutePosition(file, start, end);
                  if (marker != null) {
                    MarkUtilities.setType(marker, type);
                  } else {
                    marker = MarkerFactory.createMarker(file, (ITextSelection) selection);
                    MarkUtilities.setType(marker, type);
                    AlloyUtilities.addMarkerToRepository(marker);
                  }
                  AlloyUtilities.addTypeToMarker(marker);
                  Visualization.showViz(MetaModelEditor.modelEditor);
                }
              }
            });
          }

          @Override
          public void dragEnter(final DropTargetDragEvent dtde) {
            if (this.isDragOk(dtde) == false) {
              dtde.rejectDrag();
              return;
            }
            dtde.acceptDrag(acceptableOps);
          }

          @Override
          public void dragExit(final DropTargetEvent dte) {}

          @Override
          public void dragOver(final DropTargetDragEvent dtde) {
            if (this.isDragOk(dtde) == false) {
              dtde.rejectDrag();
              return;
            }
            dtde.acceptDrag(acceptableOps);
          }

          @SuppressWarnings("deprecation")
          @Override
          public void drop(final DropTargetDropEvent dtde) {
            final DataFlavor flavor = DataFlavor.stringFlavor;
            DataFlavor chosen = null;
            if (dtde.isLocalTransfer() == false) {
              chosen = DataFlavor.plainTextFlavor;
            } else {
              if (dtde.isDataFlavorSupported(flavor)) {
                chosen = flavor;
              }
            }
            if (chosen == null) {
              dtde.rejectDrop();
              return;
            }

            final int sa = dtde.getSourceActions();
            if ((sa & acceptableOps) == 0) {
              dtde.rejectDrop();
              return;
            }

            Object data = null;
            try {
              dtde.acceptDrop(acceptableOps);
              data = dtde.getTransferable().getTransferData(chosen);
              if (data == null) {
                throw new NullPointerException();
              }
            } catch (final Throwable t) {
              t.printStackTrace();
              dtde.dropComplete(false);
              return;
            }

            final Point mousePoint = dtde.getLocation();
            final GraphViewer graphViewer =
                (GraphViewer) dtde.getDropTargetContext().getComponent();
            final Object annotation =
                graphViewer.alloyGetAnnotationAtXY(mousePoint.x, mousePoint.y);
            if (annotation instanceof AlloyAtom) {
              final AlloyAtom atom = (AlloyAtom) annotation;
              this.createMarker(atom.toString());
              dtde.dropComplete(true);
            }
          }

          @Override
          public void dropActionChanged(final DropTargetDragEvent dtde) {
            if (this.isDragOk(dtde) == false) {
              dtde.rejectDrag();
              return;
            }
            dtde.acceptDrag(acceptableOps);
          }

          private boolean isDragOk(final DropTargetDragEvent dtde) {
            final DataFlavor flavor = DataFlavor.stringFlavor;
            DataFlavor chosen = null;

            if (dtde.isDataFlavorSupported(flavor)) {
              chosen = flavor;
            }

            if (chosen == null) {
              return false;
            }

            final int sourceActions = dtde.getSourceActions();

            if ((sourceActions & acceptableOps) == 0) {
              return false;
            }

            final Point mousePoint = dtde.getLocation();
            final GraphViewer graphViewer =
                (GraphViewer) dtde.getDropTargetContext().getComponent();
            final Object annotation =
                graphViewer.alloyGetAnnotationAtXY(mousePoint.x, mousePoint.y);
            if (annotation instanceof AlloyAtom) {
              final AlloyAtom atom = (AlloyAtom) annotation;
              if (atom.getType().isAbstract) {
                return false;
              }
            } else {
              return false;
            }
            return true;
          }
        }, true);
  }

  public void create() {
    int index;
    this.textEditor = new TextEditor();
    try {
      index = this.addPage(this.textEditor, this.getEditorInput());
      this.setPageText(index, "Source");
      this.setPartName(this.textEditor.getTitle());
    } catch (final PartInitException e) {
      ErrorDialog.openError(this.getSite().getShell(), " Error creating nested text editor", null,
          e.getStatus());
    }

    MetaModelEditor.modelEditor = new Composite(this.getContainer(), SWT.EMBEDDED);
    index = this.addPage(MetaModelEditor.modelEditor);
    this.setPageText(index, "Specification");


    @SuppressWarnings("unused")
    final AlloyParserForMetamodel alloyParserForMetamodel = new AlloyParserForMetamodel(
        ((FileEditorInput) this.textEditor.getEditorInput()).getPath().toString());

    MetaModelEditor.frame = null;
    MetaModelEditor.myState = null;
    MetaModelEditor.graph = null;
    MetaModelEditor.file = null;

    this.showMetamodel(true);
  }

  @Override
  protected void createPages() {
    this.create();
    this.addDropListener();
  }

  @Override
  public void doSave(final IProgressMonitor monitor) {
    // nothing
  }

  @Override
  public void doSaveAs() {
    // nothing
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  private void showMetamodel(final boolean isMagicLayout) {
    MetaModelEditor.xmlFileName =
        Util.canon(AlloyUtilities.getLocationForMetamodel(this.textEditor.getTitle()));

    if (!AlloyUtilities.isExists()) {
      if (MetaModelEditor.frame != null) {
        if (MetaModelEditor.frame.getComponentCount() > 0) {
          MetaModelEditor.frame.removeAll();
        }
        MetaModelEditor.frame.add(new JPanel());
      } else if (MetaModelEditor.frame == null) {
        MetaModelEditor.frame = SWT_AWT.new_Frame(MetaModelEditor.modelEditor);
        MetaModelEditor.frame.add(new JPanel());
      }
      return;
    }
    MetaModelEditor.file = new File(MetaModelEditor.xmlFileName);
    try {
      if (!MetaModelEditor.file.exists()) {
        throw new IOException("File " + MetaModelEditor.xmlFileName + " does not exist.");
      }
      // AlloyUtilities.setMetamodel(this.editor1.getTitle(), true);
      final AlloyInstance instance = StaticInstanceReader.parseInstance(MetaModelEditor.file);

      MetaModelEditor.myState = new VizState(instance);
      if (isMagicLayout == true) {
        MagicLayout.magic(MetaModelEditor.myState);
        MagicColor.magic(MetaModelEditor.myState);
      } else {
        MetaModelEditor.myState.resetTheme();
      }

      if (MetaModelEditor.frame == null) {
        MetaModelEditor.frame = SWT_AWT.new_Frame(MetaModelEditor.modelEditor);
      }

      if (MetaModelEditor.graph != null && MetaModelEditor.frame.getComponent(0) != null) {
        MetaModelEditor.frame.remove(MetaModelEditor.graph);
      }

      MetaModelEditor.graph = new VizGraphPanel(MetaModelEditor.myState, false);
      MetaModelEditor.frame.removeAll();
      MetaModelEditor.frame.add(MetaModelEditor.graph);
      MetaModelEditor.frame.setVisible(true);
      MetaModelEditor.frame.setAlwaysOnTop(true);
      MetaModelEditor.graph.alloyGetViewer().alloyRepaint();

      final JMenuItem magicLayoutMenuItem = new JMenuItem("Magic Layout");
      final JMenuItem resetThemeMenuItem = new JMenuItem("Reset Theme");

      MetaModelEditor.graph.alloyGetViewer().pop.add(magicLayoutMenuItem, 0);
      MetaModelEditor.graph.alloyGetViewer().pop.add(resetThemeMenuItem, 1);

      magicLayoutMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(final ActionEvent e) {
          MetaModelEditor.this.showMetamodel(true);
        }
      });

      resetThemeMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(final ActionEvent e) {
          MetaModelEditor.this.showMetamodel(false);
        }
      });
    } catch (final Err e1) {
      e1.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}