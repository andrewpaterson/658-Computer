package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class OrGateViewFactory
    extends ViewFactory<OrGateView, LogicGateProperties>
{
  @Override
  public OrGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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
  public OrGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LogicGateProperties properties)
  {
    return new OrGateView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<OrGateView> getViewClass()
  {
    return OrGateView.class;
  }
}

