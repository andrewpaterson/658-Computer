package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class LeaveSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Leave Subcircuit";

  public LeaveSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.leaveSubcircuit();
  }

  @Override
  public boolean isAvailable()
  {
    return false;
  }
}

