package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class GotoSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public GotoSubcircuitAction(SimulatorEditor editor, int index)
  {
    super(editor);
    this.index = index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoSubcircuit(index);
  }

  @Override
  public String getDescription()
  {
    return "Goto Subcircuit Bookmark";
  }
}

