package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PasteAction
    extends SimulatorEditorAction
{
  public PasteAction(Logicim editor)
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

