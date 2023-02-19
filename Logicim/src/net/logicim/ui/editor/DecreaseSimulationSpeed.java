package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DecreaseSimulationSpeed
    extends SimulatorEditorAction
{
  public DecreaseSimulationSpeed(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.decreaseSimulationSpeed();
  }

  @Override
  public String getDescription()
  {
    return "Decrease Simulation Speed";
  }
}
