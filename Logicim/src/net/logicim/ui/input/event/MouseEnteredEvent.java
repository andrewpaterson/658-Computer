package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

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
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mouseEntered(x, y);
  }
}

