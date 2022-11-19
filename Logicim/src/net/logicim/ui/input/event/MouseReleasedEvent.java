package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

public class MouseReleasedEvent
    extends SimulatorEditorEvent
{
  protected int x;
  protected int y;
  protected int button;

  public MouseReleasedEvent(int x, int y, int button)
  {
    super();
    this.x = x;
    this.y = y;
    this.button = button;
  }

  @Override
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mouseReleased(x, y, button);
  }
}

