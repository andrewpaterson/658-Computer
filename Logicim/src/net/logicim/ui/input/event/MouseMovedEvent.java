package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class MouseMovedEvent
    extends SimulatorEditorEvent
{
  private int x;
  private int y;

  public MouseMovedEvent(int x, int y)
  {
    super();
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(Logicim logicim)
  {
    logicim.mouseMoved(x, y);
  }
}

