package eu.modelwriter.marker.command.visualization;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import eu.modelwriter.configuration.alloy.analysis.StaticAlloyAnalysisManager;
import eu.modelwriter.marker.ui.internal.views.visualizationview.Visualization;

public class VizReasonRelationsHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    final Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        if (!StaticAlloyAnalysisManager.startReasoning()) {
          JOptionPane.showMessageDialog(null, "There is not any reasoning.", "Reasoning Relations",
              JOptionPane.INFORMATION_MESSAGE);
        }
        Visualization.showViz();
      }
    });
    thread.start();
    return true;
  }
}