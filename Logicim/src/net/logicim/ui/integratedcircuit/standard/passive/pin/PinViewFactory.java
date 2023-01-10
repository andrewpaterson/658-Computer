package net.logicim.ui.integratedcircuit.standard.passive.pin;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.integratedcircuit.standard.passive.pin.PinProperties;
import net.logicim.ui.integratedcircuit.standard.passive.pin.PinView;

public class PinViewFactory extends ViewFactory<PinView, PinProperties>
{
  @Override
  public PinView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new PinView(circuitEditor,
                            position,
                            rotation,
                            new PinProperties());
  }

  @Override
  public PinView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PinProperties properties)
  {
    return new PinView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<PinView> getViewClass()
  {
    return PinView.class;
  }
}

