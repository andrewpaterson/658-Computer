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

  @Override
  public void executeEditorAction()
  {
    editor.bookmarkSubcircuit(index);
  }
}

