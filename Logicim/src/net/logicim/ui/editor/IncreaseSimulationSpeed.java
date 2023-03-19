package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class IncreaseSimulationSpeed
    extends SimulatorEditorAction
{
  public IncreaseSimulationSpeed(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.increaseSimulationSpeed();
  }

  @Override
  public String getDescription()
  {
    return "Increase Simulation Speed";
  }
}
