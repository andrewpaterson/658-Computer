package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class ReenterSubcircuitAction
    extends SimulatorEditorAction
{
  public ReenterSubcircuitAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.reenterSubcircuit();
  }

  @Override
  public String getDescription()
  {
    return "Re-enter Subcircuit";
  }
}

