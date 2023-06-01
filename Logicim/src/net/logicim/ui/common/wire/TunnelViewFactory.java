package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class TunnelViewFactory
    extends ViewFactory<TunnelView, TunnelProperties>
{
  @Override
  public TunnelView create(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation)
  {
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public TunnelProperties createInitialProperties()
  {
    return new TunnelProperties("  ", false);
  }

  @Override
  public TunnelView create(CircuitEditor circuitEditor,
                           SubcircuitView subcircuitView,
                           Int2D position,
                           Rotation rotation,
                           TunnelProperties properties)
  {
    return new TunnelView(subcircuitView,
                          position,
                          rotation,
                          properties);
  }

  @Override
  public Class<TunnelView> getViewClass()
  {
    return TunnelView.class;
  }
}

