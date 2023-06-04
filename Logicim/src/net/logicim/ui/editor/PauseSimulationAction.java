package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PauseSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Pause Simulation";

  public PauseSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.setRunning(false);
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canPauseSimulation();
  }
}

