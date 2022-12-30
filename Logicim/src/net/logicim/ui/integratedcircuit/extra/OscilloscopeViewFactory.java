package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Units;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

public class OscilloscopeViewFactory
    extends ViewFactory
{

  public OscilloscopeViewFactory()
  {
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new OscilloscopeView(circuitEditor,
                                4,
                                32,
                                4,
                                30,
                                6 * Units.GHz,
                                position,
                                rotation,
                                "",
                                DefaultFamily.get());
  }

  @Override
  public Class<? extends DiscreteView<?>> getViewClass()
  {
    return OscilloscopeView.class;
  }
}

