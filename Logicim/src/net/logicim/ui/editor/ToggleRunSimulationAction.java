package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ToggleRunSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Toggle Simulation Run";

  public ToggleRunSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.toggleTunSimulation();
  }
}

