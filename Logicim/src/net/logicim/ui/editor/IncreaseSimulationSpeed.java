package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class IncreaseSimulationSpeed extends EditorAction
{
  public IncreaseSimulationSpeed(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.increaseSimulationSpeed();
  }
}
