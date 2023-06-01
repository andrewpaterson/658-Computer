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
    boolean componentDeleted = editor.editActionDeleteComponentIfPossible();
    if (componentDeleted)
    {
      editor.getCircuitEditor().circuitUpdated();
      editor.pushUndo();
    }

    editor.updateHighlighted();
  }

  @Override
  public String getDescription()
  {
    return "Delete";
  }
}

