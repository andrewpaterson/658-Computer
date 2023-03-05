package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class BookmarkSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public BookmarkSubcircuitAction(SimulatorEditor editor, int index)
  {
    super(editor);
    this.index = index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.bookmarkSubcircuit(index);
  }

  @Override
  public String getDescription()
  {
    return "Bookmark Subcircuit";
  }
}

