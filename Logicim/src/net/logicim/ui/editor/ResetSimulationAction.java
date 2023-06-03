package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ResetSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Reset Simulation";

  public ResetSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.resetSimulation();
  }
}

