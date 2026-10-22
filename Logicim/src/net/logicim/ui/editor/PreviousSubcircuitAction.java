package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PreviousSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Previous Subcircuit";

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
  public boolean isAvailable()
  {
    return editor.canGotoNextSubcircuit();
  }
}

