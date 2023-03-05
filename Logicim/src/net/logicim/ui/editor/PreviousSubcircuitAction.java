package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PreviousSubcircuitAction
    extends SimulatorEditorAction
{
  public PreviousSubcircuitAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.gotoPreviousSubcircuit();
  }

  @Override
  public String getDescription()
  {
    return "Previous Subcircuit";
  }
}

