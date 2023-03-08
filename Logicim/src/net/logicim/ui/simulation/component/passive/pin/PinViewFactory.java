package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class PinViewFactory
    extends ViewFactory<PinView, PinProperties>
{
  @Override
  public PinView create(SubcircuitView subcircuitView,
                        Circuit circuit,
                        Int2D position,
                        Rotation rotation)
  {
    return new PinView(subcircuitView,
                       circuit,
                       position,
                       rotation,
                       new PinProperties("",
                                         1,
                                         SubcircuitPinAlignment.LEFT,
                                         false,
                                         false,
                                         false));
  }

  @Override
  public PinView create(SubcircuitView subcircuitView,
                        Circuit circuit,
                        Int2D position,
                        Rotation rotation,
                        PinProperties properties)
  {
    return new PinView(subcircuitView,
                       circuit,
                       position,
                       rotation,
                       properties);
  }

  @Override
  public Class<PinView> getViewClass()
  {
    return PinView.class;
  }
}

