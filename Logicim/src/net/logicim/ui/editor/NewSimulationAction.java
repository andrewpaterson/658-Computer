package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.panels.SimulatorPanel;

public class NewSimulationAction
    extends EditorAction
{
  public static final String NAME = "New Simulation";

  private SimulatorPanel panel;

  public NewSimulationAction(Logicim editor, SimulatorPanel panel)
  {
    super(editor);
    this.panel = panel;
  }

  @Override
  public void executeEditorAction()
  {
    panel.newSimulation();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

