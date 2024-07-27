package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class GotoSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public GotoSubcircuitAction(Logicim editor, int index)
  {
    super(editor);
    this.index = index;
  }

  public static String name(int index)
  {
    return "Goto Subcircuit Bookmark " + index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoSubcircuit(index);
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canGotoSubcircuit(index);
  }
}

