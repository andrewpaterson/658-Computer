package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class GroundViewFactory
    extends ViewFactory<GroundView, GroundProperties>
{
  @Override
  public GroundView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation)
  {
    return new GroundView(subcircuitView,
                          circuit,
                          position,
                          rotation,
                          new GroundProperties(""));
  }

  @Override
  public GroundView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation,
                           GroundProperties properties)
  {
    return new GroundView(subcircuitView,
                          circuit,
                          position,
                          rotation,
                          properties);
  }

  @Override
  public Class<GroundView> getViewClass()
  {
    return GroundView.class;
  }
}

