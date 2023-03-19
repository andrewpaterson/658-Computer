package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class DuplicateAction
    extends SimulatorEditorAction
{
  public DuplicateAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionDuplicate();
  }

  @Override
  public String getDescription()
  {
    return "Duplicate";
  }
}

