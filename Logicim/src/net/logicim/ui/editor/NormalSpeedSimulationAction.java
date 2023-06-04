package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class NormalSpeedSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Set Simulation Default Speed";

  public NormalSpeedSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.setDefaultSimulationSpeed();
  }

  @Override
  public boolean isAvailable()
  {
    return true;
  }
}

