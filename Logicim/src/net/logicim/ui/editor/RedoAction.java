package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class RedoAction
    extends SimulatorEditorAction
{
  public RedoAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.redo();
  }
}

