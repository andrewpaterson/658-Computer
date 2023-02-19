package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DeleteAction
    extends SimulatorEditorAction
{
  public DeleteAction(SimulatorEditor editor)
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

