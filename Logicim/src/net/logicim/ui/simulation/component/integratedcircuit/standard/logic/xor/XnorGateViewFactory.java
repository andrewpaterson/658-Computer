package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class XnorGateViewFactory
    extends ViewFactory<XnorGateView, LogicGateProperties>
{
  @Override
  public XnorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new LogicGateProperties("",
                                          DefaultFamily.get(),
                                          true,
                                          2,
                                          1));
  }

  @Override
  public XnorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LogicGateProperties properties)
  {
    return new XnorGateView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<XnorGateView> getViewClass()
  {
    return XnorGateView.class;
  }
}

