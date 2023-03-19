package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class CutAction
    extends SimulatorEditorAction
{
  public CutAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionCut();
  }

  @Override
  public String getDescription()
  {
    return "Cut";
  }
}

