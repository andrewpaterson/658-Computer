package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PasteAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Paste";

  public PasteAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionPaste();
  }
}

