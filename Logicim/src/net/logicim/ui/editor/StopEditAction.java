package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class StopEditAction
    extends SimulatorEditorAction
{
  public StopEditAction(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.stopSimulatorEdit();
  }

  @Override
  public String getDescription()
  {
    return "Discard Current Edit";
  }
}

