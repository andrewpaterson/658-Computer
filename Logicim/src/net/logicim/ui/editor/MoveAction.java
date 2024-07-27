package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class MoveAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Move";

  public MoveAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionMove();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canMove();
  }
}

