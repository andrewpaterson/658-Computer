package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class XorGateViewFactory
    extends ViewFactory<XorGateView, LogicGateProperties>
{
  @Override
  public XorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  new LogicGateProperties("",
                                          DefaultFamily.get(),
                                          true,
                                          2));
  }

  @Override
  public XorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LogicGateProperties properties)
  {
    return new XorGateView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<XorGateView> getViewClass()
  {
    return XorGateView.class;
  }
}

