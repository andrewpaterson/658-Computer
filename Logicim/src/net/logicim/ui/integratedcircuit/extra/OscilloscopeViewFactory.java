package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Units;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.*;

public class OscilloscopeViewFactory
    extends ViewFactory
{
  private Colours colours;

  public OscilloscopeViewFactory(Colours colours)
  {
    this.colours = colours;
  }

  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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
}

