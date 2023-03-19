package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class MouseExitedEvent
    extends SimulatorEditorEvent
{
  @Override
  public void execute(Logicim logicim)
  {
    logicim.mouseExited();
  }
}

