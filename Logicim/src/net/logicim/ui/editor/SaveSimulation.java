package net.logicim.ui.editor;

import net.logicim.ui.SimulatorPanel;

public class SaveSimulation
    extends EditorAction
{
  private SimulatorPanel panel;

  public SaveSimulation(SimulatorPanel panel)
  {
    this.panel = panel;
  }

  @Override
  public void execute()
  {
    panel.saveSimulation();
  }
}
