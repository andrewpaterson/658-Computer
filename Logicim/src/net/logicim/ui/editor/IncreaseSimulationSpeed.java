package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class IncreaseSimulationSpeed
    extends SimulatorEditorAction
{
  public IncreaseSimulationSpeed(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.increaseSimulationSpeed();
  }
}
