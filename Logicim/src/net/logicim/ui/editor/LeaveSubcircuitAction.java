package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class LeaveSubcircuitAction
    extends SimulatorEditorAction
{
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
  public String getDescription()
  {
    return "Leave Subcircuit";
  }
}

