package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class IncreaseSimulationSpeedAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Increase Simulation Speed";

  public IncreaseSimulationSpeedAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.increaseSimulationSpeed();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}
