package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PreviousSubcircuitAction
    extends SimulatorEditorAction
{
  public PreviousSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoPreviousSubcircuit();
  }

  @Override
  public String getDescription()
  {
    return "Previous Subcircuit";
  }
}

