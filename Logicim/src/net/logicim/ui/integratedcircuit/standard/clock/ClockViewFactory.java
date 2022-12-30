package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

import static net.logicim.domain.common.Units.MHz;

public class ClockViewFactory
    extends ViewFactory
{
  @Override
  public ClockView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new ClockView(circuitEditor,
                         position,
                         rotation,
                         "",
                         DefaultFamily.get(),
                         25 * MHz,
                         false,
                         true);
  }

  @Override
  public Class<? extends DiscreteView<?>> getViewClass()
  {
    return ClockView.class;
  }
}

