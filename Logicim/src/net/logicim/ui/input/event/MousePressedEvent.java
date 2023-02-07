package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class MousePressedEvent
    extends SimulatorEditorEvent
{
  protected int x;
  protected int y;
  protected int button;
  protected int clickCount;

  public MousePressedEvent(int x, int y, int button, int clickCount)
  {
    super();
    this.x = x;
    this.y = y;
    this.button = button;
    this.clickCount = clickCount;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mousePressed(x, y, button, clickCount);
  }
}

