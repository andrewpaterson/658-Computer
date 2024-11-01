package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class HaltSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Halt Simulation";

  public HaltSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

