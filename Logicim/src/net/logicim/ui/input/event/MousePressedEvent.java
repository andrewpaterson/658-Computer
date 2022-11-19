package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class MousePressedEvent
    extends SimulatorEditorEvent
{
  protected int x;
  protected int y;
  protected int button;

  public MousePressedEvent(int x, int y, int button)
  {
    super();
    this.x = x;
    this.y = y;
    this.button = button;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mousePressed(x, y, button);
  }
}

