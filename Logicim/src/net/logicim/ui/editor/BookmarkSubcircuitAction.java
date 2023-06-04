package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class BookmarkSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public BookmarkSubcircuitAction(Logicim editor, int index)
  {
    super(editor);
    this.index = index;
  }

  public static String name(int index)
  {
    return "Bookmark Subcircuit " + index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.bookmarkSubcircuit(index);
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

