package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class DeleteAction
    extends SimulatorEditorAction
{
  public DeleteAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionDelete();
  }

  @Override
  public String getDescription()
  {
    return "Delete";
  }
}

