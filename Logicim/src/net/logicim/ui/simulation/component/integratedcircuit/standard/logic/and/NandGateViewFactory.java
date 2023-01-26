package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class NandGateViewFactory
    extends ViewFactory<NandGateView, LogicGateProperties>
{
  @Override
  public NandGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
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
  public NandGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LogicGateProperties properties)
  {
    return new NandGateView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<NandGateView> getViewClass()
  {
    return NandGateView.class;
  }
}

