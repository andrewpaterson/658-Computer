package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.SimulatorPanel;

public class SelectSimulationAction
    extends EditorAction
{
  public static final String NAME = "Select Simulation";

  private SimulatorPanel panel;

  public SelectSimulationAction(Logicim editor, SimulatorPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.selectSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

