package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class DecreaseSimulationSpeedAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Decrease Simulation Speed";

  public DecreaseSimulationSpeedAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.decreaseSimulationSpeed();
  }
}

