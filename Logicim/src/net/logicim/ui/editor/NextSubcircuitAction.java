package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class NextSubcircuitAction
    extends SimulatorEditorAction
{
  public NextSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoNextSubcircuit();
  }

  @Override
  public String getDescription()
  {
    return "Next Subcircuit";
  }
}

