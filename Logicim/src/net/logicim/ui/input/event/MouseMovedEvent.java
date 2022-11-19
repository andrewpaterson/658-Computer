package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

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
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mouseMoved(x, y);
  }
}

