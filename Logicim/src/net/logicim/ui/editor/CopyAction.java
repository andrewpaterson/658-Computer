package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class CopyAction
    extends SimulatorEditorAction
{
  public CopyAction(SimulatorEditor editor)
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

