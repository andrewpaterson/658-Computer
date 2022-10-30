package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.View;
import net.logicim.ui.common.ViewFactory;

public class ClockViewFactory
    extends ViewFactory
{
  @Override
  public View create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new ClockView(circuitEditor, position, rotation);
  }
}

