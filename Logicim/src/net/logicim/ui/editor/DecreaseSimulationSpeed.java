package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DecreaseSimulationSpeed
    extends EditorAction
{
  public DecreaseSimulationSpeed(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.decreaseSimulationSpeed();
  }
}
