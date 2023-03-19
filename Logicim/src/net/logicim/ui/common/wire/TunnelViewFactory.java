package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
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
    return create(circuitEditor,
                  position,
                  rotation,
                  new TunnelProperties("  ",
                                       false));
  }

  @Override
  public TunnelView create(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           TunnelProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    return new TunnelView(subcircuitView,
                          circuit,
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

