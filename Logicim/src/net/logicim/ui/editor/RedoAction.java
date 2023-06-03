package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RedoAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Redo";

  public RedoAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.redo();
  }
}

