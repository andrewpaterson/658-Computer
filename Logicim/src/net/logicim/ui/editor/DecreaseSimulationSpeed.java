package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class DecreaseSimulationSpeed
    extends SimulatorEditorAction
{
  public DecreaseSimulationSpeed(Logicim editor)
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
