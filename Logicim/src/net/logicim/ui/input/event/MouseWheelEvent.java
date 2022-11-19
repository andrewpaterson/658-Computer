package net.logicim.ui.input.event;

import net.logicim.ui.SimulatorEditor;

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
  public void execute(SimulatorEditor simulatorEditor)
  {
    simulatorEditor.mouseWheel(wheelRotation);
  }
}

