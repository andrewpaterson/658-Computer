package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class LeaveSubcircuitAction
    extends SimulatorEditorAction
{
  public LeaveSubcircuitAction(SimulatorEditor editor)
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

