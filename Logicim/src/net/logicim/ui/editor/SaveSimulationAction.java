package net.logicim.ui.editor;

import net.logicim.ui.panels.SimulatorPanel;

public class SaveSimulationAction
    extends EditorAction
{
  public static final String NAME = "Save Project";

  private SimulatorPanel panel;

  public SaveSimulationAction(SimulatorPanel panel)
  {
    super(panel.getEditor());
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.saveSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

