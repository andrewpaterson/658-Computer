package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Units;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;

public class OscilloscopeViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new OscilloscopeView(circuitEditor, 4, 32, 4, 5, 10 * Units.GHz, position, rotation, "");
  }
}

