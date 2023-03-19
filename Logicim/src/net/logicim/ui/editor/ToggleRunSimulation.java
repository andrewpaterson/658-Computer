package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class ToggleRunSimulation
    extends SimulatorEditorAction
{
  public ToggleRunSimulation(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.toggleTunSimulation();
  }

  @Override
  public String getDescription()
  {
    return "Toggle Simulation Run";
  }
}

