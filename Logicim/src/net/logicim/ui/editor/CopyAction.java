package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class CopyAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Copy";

  public CopyAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionCopy();
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canCopy();
  }
}

