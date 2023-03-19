package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ReenterSubcircuitAction
    extends SimulatorEditorAction
{
  public ReenterSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.reenterSubcircuit();
  }

  @Override
  public String getDescription()
  {
    return "Re-enter Subcircuit";
  }
}

