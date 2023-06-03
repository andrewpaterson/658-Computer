package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class CutAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Cut";

  public CutAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionCut();
  }
}

