package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DuplicateAction
    extends SimulatorEditorAction
{
  public DuplicateAction(SimulatorEditor editor)
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

