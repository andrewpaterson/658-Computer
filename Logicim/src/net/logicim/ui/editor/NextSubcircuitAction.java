package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class NextSubcircuitAction
    extends SimulatorEditorAction
{
  public NextSubcircuitAction(SimulatorEditor editor)
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

