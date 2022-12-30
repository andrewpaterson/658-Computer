package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;

public class GroundViewFactory
    extends ViewFactory
{
  @Override
  public GroundView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new GroundView(circuitEditor, position, rotation, "");
  }

  @Override
  public Class<? extends DiscreteView<?>> getViewClass()
  {
    return GroundView.class;
  }
}

