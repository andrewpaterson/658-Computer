package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class UndoAction
    extends SimulatorEditorAction
{
  public UndoAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.undo();
  }

  @Override
  public String getDescription()
  {
    return "Undo";
  }
}

