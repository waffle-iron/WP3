package eu.modelwriter.marker.command;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import eu.modelwriter.marker.MarkerActivator;
import eu.modelwriter.marker.internal.MarkerFactory;
import eu.modelwriter.marker.internal.MarkerUpdater;
import eu.modelwriter.marker.ui.internal.wizards.markerwizard.MarkerWizard;
import eu.modelwriter.marker.ui.internal.wizards.selectionwizard.SelectionWizard;

public class AddTypeToMarkerHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection selection =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    IFile file = (IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor().getEditorInput().getAdapter(IFile.class);

    if (selection instanceof ITextSelection) {
      ITextSelection textSelection = (ITextSelection) selection;
      ArrayList<IMarker> markerList = MarkerFactory.findMarkersInSelection(file, textSelection);
      if (markerList != null) {
        IMarker selectedMarker = null;
        if (markerList.size() == 1) {
          selectedMarker = markerList.get(0);
        } else if (markerList.size() > 1) {
          SelectionWizard selectionWizard = new SelectionWizard(markerList);
          WizardDialog selectionDialog =
              new WizardDialog(MarkerActivator.getShell(), selectionWizard);
          if (selectionDialog.open() == 1)
            return null;
          selectedMarker = selectionWizard.getSelectedMarker();
        }

        MarkerWizard markerWizard = new MarkerWizard(selectedMarker);

        WizardDialog dialog = new WizardDialog(MarkerActivator.getShell(), markerWizard);

        if (dialog.open() == org.eclipse.jface.window.Window.OK) {
          System.out.println("Ok pressed");
        } else {
          System.out.println("Cancel pressed");
        }

        MarkerUpdater.updateTargets(selectedMarker);
        MarkerUpdater.updateSources(selectedMarker);
      }
    } else if (selection instanceof TreeSelection) {
      ITreeSelection treeSelection = (ITreeSelection) selection;
      IEditorPart editor =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
      if (selection != null && editor instanceof EcoreEditor) {
        if (treeSelection.getFirstElement() instanceof ENamedElement
            && ((ENamedElement) treeSelection.getFirstElement()).getName() != null
            && !((ENamedElement) treeSelection.getFirstElement()).getName().isEmpty()) {
          URI uri = EcoreUtil.getURI((ENamedElement) treeSelection.getFirstElement());

          IMarker selectedMarker;

          selectedMarker = MarkerFactory.findMarkersByUri(file, uri.toString());

          if (selectedMarker != null && selectedMarker.exists()) {
            MarkerWizard markerWizard = new MarkerWizard(selectedMarker);

            WizardDialog dialog = new WizardDialog(MarkerActivator.getShell(), markerWizard);

            if (dialog.open() == org.eclipse.jface.window.Window.OK) {
              System.out.println("Ok pressed");
            } else {
              System.out.println("Cancel pressed");
            }

            MarkerUpdater.updateTargets(selectedMarker);
            MarkerUpdater.updateSources(selectedMarker);
          } else {
            MessageDialog dialog = new MessageDialog(MarkerActivator.getShell(),
                "There is no marker in this position", null, "Please select valid marker",
                MessageDialog.INFORMATION, new String[] {"OK"}, 0);
            dialog.open();
          }
        } else if (!((EObject) treeSelection.getFirstElement() instanceof EModelElement)) {
          URI uri = EcoreUtil.getURI((EObject) treeSelection.getFirstElement());

          IMarker selectedMarker;
          selectedMarker = MarkerFactory.findMarkersByUri(file, uri.toString());

          if (selectedMarker != null && selectedMarker.exists()) {
            MarkerWizard markerWizard = new MarkerWizard(selectedMarker);

            WizardDialog dialog = new WizardDialog(MarkerActivator.getShell(), markerWizard);

            if (dialog.open() == org.eclipse.jface.window.Window.OK) {
              System.out.println("Ok pressed");
            } else {
              System.out.println("Cancel pressed");
            }

            MarkerUpdater.updateTargets(selectedMarker);
            MarkerUpdater.updateSources(selectedMarker);
          } else {
            MessageDialog dialog = new MessageDialog(MarkerActivator.getShell(),
                "There is no marker in this position", null, "Please select valid marker",
                MessageDialog.INFORMATION, new String[] {"OK"}, 0);
            dialog.open();
          }
        }
      }
    }

    return null;
  }
}