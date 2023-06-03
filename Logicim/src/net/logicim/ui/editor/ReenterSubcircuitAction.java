package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ReenterSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Re-enter Subcircuit";

  public ReenterSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.reenterSubcircuit();
  }
}

