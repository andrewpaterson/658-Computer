package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.View;
import net.logicim.ui.common.ViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.NotGateView;

public class NotGateViewFactory
    extends ViewFactory
{
  @Override
  public View create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new NotGateView(circuitEditor, position, rotation);
  }
}

