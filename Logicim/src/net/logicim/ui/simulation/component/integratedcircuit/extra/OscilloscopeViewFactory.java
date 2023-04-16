package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.extra.OscilloscopeProperties;
import net.logicim.domain.common.Units;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class OscilloscopeViewFactory
    extends ViewFactory<OscilloscopeView, OscilloscopeProperties>
{

  public OscilloscopeViewFactory()
  {
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor,
                                 Int2D position,
                                 Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  Rotation.Cannot,
                  createDefaultProperties(getViewClass()));
  }

  @Override
  public OscilloscopeProperties createInitialProperties()
  {
    return new OscilloscopeProperties(null,
                                      DefaultFamily.get(),
                                      2,
                                      32,
                                      4,
                                      30,
                                      6 * Units.GHz);
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor,
                                 Int2D position,
                                 Rotation rotation,
                                 OscilloscopeProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    return new OscilloscopeView(subcircuitView,
                                position,
                                rotation,
                                properties);
  }

  @Override
  public Class<OscilloscopeView> getViewClass()
  {
    return OscilloscopeView.class;
  }
}

