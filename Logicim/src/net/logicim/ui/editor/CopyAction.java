package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class CopyAction
    extends SimulatorEditorAction
{
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
  public String getDescription()
  {
    return "Copy";
  }
}

