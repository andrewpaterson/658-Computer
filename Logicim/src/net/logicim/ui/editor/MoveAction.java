package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class MoveAction
    extends SimulatorEditorAction
{
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
  public String getDescription()
  {
    return "Move";
  }
}

