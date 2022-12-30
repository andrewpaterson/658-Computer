package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NorGateViewFactory
    extends ViewFactory<NorGateView, LogicGateProperties>
{
  @Override
  public NorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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
  public NorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LogicGateProperties properties)
  {
    return new NorGateView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<NorGateView> getViewClass()
  {
    return NorGateView.class;
  }
}

