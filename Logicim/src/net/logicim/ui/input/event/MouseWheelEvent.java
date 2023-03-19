package net.logicim.ui.input.event;

import net.logicim.ui.Logicim;

public class MouseWheelEvent
    extends SimulatorEditorEvent
{
  private int wheelRotation;

  public MouseWheelEvent(int wheelRotation)
  {
    super();
    this.wheelRotation = wheelRotation;
  }

  @Override
  public void execute(Logicim logicim)
  {
    logicim.mouseWheel(wheelRotation);
  }
}

