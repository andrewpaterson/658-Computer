package net.logicim.ui.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;

public class XnorGateViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new XnorGateView(circuitEditor, 2, position, rotation);
  }
}
