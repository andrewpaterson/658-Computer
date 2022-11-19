package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class MouseExitedEvent
    extends SimulatorEditorEvent
{
  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mouseExited();
  }
}

