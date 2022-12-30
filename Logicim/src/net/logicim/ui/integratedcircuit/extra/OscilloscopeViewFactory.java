package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Units;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;

public class OscilloscopeViewFactory
    extends ViewFactory<OscilloscopeView, OscilloscopeProperties>
{

  public OscilloscopeViewFactory()
  {
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new OscilloscopeProperties("",
                                             DefaultFamily.get(),
                                             4,
                                             32,
                                             4,
                                             30,
                                             6 * Units.GHz));
  }

  @Override
  public OscilloscopeView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, OscilloscopeProperties properties)
  {
    return new OscilloscopeView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<OscilloscopeView> getViewClass()
  {
    return OscilloscopeView.class;
  }
}

