package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class OrGateViewFactory
    extends ViewFactory<OrGateView, LogicGateProperties>
{
  @Override
  public OrGateView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation)
  {
    return create(subcircuitView,
                  circuit,
                  position,
                  rotation,
                  new LogicGateProperties("",
                                          DefaultFamily.get(),
                                          true,
                                          2,
                                          1));
  }

  @Override
  public OrGateView create(SubcircuitView subcircuitView,
                           Circuit circuit,
                           Int2D position,
                           Rotation rotation,
                           LogicGateProperties properties)
  {
    return new OrGateView(subcircuitView,
                          circuit,
                          position,
                          rotation,
                          properties);
  }

  @Override
  public Class<OrGateView> getViewClass()
  {
    return OrGateView.class;
  }
}

