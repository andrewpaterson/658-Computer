package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RecreateSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Recreate Simulation";

  public RecreateSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.createAndUnhaltSimulations();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

