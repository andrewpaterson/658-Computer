package net.logicim.ui.simulation.component.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class GroundViewFactory
    extends ViewFactory<GroundView, GroundProperties>
{
  @Override
  public GroundView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new GroundView(circuitEditor,
                          position,
                          rotation,
                          new GroundProperties(""));
  }

  @Override
  public GroundView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, GroundProperties properties)
  {
    return new GroundView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<GroundView> getViewClass()
  {
    return GroundView.class;
  }
}

