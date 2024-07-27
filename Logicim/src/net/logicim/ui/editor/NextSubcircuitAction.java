package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class NextSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Next Subcircuit";

  public NextSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoNextSubcircuit();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canGotoNextSubcircuit();
  }
}

