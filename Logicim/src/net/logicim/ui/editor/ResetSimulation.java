package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ResetSimulation
    extends SimulatorEditorAction
{
  public ResetSimulation(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.resetSimulation();
  }

  @Override
  public String getDescription()
  {
    return "Reset";
  }
}

