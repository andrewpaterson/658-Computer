package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class MoveAction
    extends SimulatorEditorAction
{
  public MoveAction(SimulatorEditor editor)
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

