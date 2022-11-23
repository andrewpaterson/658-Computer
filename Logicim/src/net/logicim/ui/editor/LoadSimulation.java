package net.logicim.ui.editor;

import net.logicim.ui.SimulatorPanel;

public class LoadSimulation
    extends EditorAction
{
  private SimulatorPanel panel;

  public LoadSimulation(SimulatorPanel panel)
  {
    this.panel = panel;
  }

  @Override
  public void execute()
  {
    panel.loadSimulation();
  }
}

