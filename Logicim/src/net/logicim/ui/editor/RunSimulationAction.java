package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class RunSimulationAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Run Simulation";

  public RunSimulationAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.setRunning(true);
  }

  @Override
  public boolean isAvailable()
  {
    return editor.canRunSimulation();
  }
}

