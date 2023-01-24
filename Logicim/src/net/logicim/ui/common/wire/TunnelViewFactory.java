package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class TunnelViewFactory
    extends ViewFactory<TunnelView, TunnelProperties>
{
  @Override
  public TunnelView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new TunnelView(circuitEditor,
                          position,
                          rotation,
                          new TunnelProperties("  ",
                                               false));
  }

  @Override
  public TunnelView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, TunnelProperties properties)
  {
    return new TunnelView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<TunnelView> getViewClass()
  {
    return TunnelView.class;
  }
}

