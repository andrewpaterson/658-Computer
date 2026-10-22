package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class MouseEnteredEvent
    extends SimulatorEditorEvent
{
  private int x;
  private int y;

  public MouseEnteredEvent(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(Logicim logicim)
  {
    logicim.mouseEntered(x, y);
  }
}

