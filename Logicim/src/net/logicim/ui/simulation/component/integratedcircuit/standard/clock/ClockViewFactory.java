package net.logicim.ui.simulation.component.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.component.factory.ViewFactory;

import static net.logicim.domain.common.Units.MHz;

public class ClockViewFactory
    extends ViewFactory<ClockView, ClockProperties>
{
  @Override
  public ClockView create(SubcircuitView subcircuitView,
                          Circuit circuit,
                          Int2D position,
                          Rotation rotation)
  {
    return create(subcircuitView,
                  circuit,
                  position,
                  rotation,
                  new ClockProperties("",
                                      DefaultFamily.get(),
                                      true,
                                      25 * MHz,
                                      false));
  }

  @Override
  public ClockView create(SubcircuitView subcircuitView,
                          Circuit circuit,
                          Int2D position,
                          Rotation rotation,
                          ClockProperties properties)
  {
    return new ClockView(subcircuitView,
                         circuit,
                         position,
                         rotation,
                         properties);
  }

  @Override
  public Class<ClockView> getViewClass()
  {
    return ClockView.class;
  }
}

