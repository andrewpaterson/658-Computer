package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PasteAction
    extends SimulatorEditorAction
{
  public PasteAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionPaste();
  }

  @Override
  public String getDescription()
  {
    return "Paste";
  }
}

