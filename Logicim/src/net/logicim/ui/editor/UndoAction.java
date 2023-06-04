package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class UndoAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Undo";

  public UndoAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.undo();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canUndo();
  }
}

