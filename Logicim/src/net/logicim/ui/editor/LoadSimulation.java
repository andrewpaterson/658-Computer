package net.logicim.ui.editor;

import net.logicim.ui.panels.SimulatorPanel;

public class LoadSimulation
    extends EditorAction
{
  private SimulatorPanel panel;

  public LoadSimulation(SimulatorPanel panel)
  {
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.loadSimulation();
  }

  @Override
  public String getDescription()
  {
    return "Load";
  }
}

