package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RedoAction
    extends SimulatorEditorAction
{
  public RedoAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.redo();
  }

  @Override
  public String getDescription()
  {
    return "Redo";
  }
}

