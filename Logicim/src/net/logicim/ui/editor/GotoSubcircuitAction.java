package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class GotoSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public GotoSubcircuitAction(Logicim editor, int index)
  {
    super(editor);
    this.index = index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoSubcircuit(index);
  }
}

