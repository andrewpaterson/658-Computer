package net.logicim.ui.simulation.component.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

import static net.logicim.domain.common.Units.MHz;

public class ClockViewFactory
    extends ViewFactory<ClockView, ClockProperties>
{
  @Override
  public ClockView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new ClockProperties("",
                                      DefaultFamily.get(),
                                      true,
                                      25 * MHz,
                                      false));
  }

  @Override
  public ClockView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, ClockProperties properties)
  {
    return new ClockView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<ClockView> getViewClass()
  {
    return ClockView.class;
  }
}

