package net.logicim.ui.editor;

import net.logicim.ui.panels.SimulatorPanel;

public class LoadSimulationAction
    extends EditorAction
{
  public static final String NAME = "Load Project";

  private SimulatorPanel panel;

  public LoadSimulationAction(SimulatorPanel panel)
  {
    super(panel.getEditor());
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.loadSimulation();
  }
}

