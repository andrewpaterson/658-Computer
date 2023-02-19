package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class CutAction
    extends SimulatorEditorAction
{
  public CutAction(SimulatorEditor editor)
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

