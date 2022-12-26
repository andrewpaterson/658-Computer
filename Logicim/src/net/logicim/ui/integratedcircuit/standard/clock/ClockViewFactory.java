package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.component.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

import static net.logicim.domain.common.Units.MHz;

public class ClockViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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
}

