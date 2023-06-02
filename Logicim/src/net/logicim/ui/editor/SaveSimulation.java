package net.logicim.ui.editor;

import net.logicim.ui.panels.SimulatorPanel;

public class SaveSimulation
    extends EditorAction
{
  private SimulatorPanel panel;

  public SaveSimulation(SimulatorPanel panel)
  {
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.saveSimulation();
  }

  @Override
  public String getDescription()
  {
    return "Save";
  }
}

