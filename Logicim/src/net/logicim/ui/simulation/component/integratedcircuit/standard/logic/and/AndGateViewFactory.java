package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class AndGateViewFactory
    extends ViewFactory<AndGateView, LogicGateProperties>
{
  @Override
  public AndGateView create(SubcircuitView subcircuitView,
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
  public AndGateView create(SubcircuitView subcircuitView,
                            Circuit circuit,
                            Int2D position,
                            Rotation rotation,
                            LogicGateProperties properties)
  {
    return new AndGateView(subcircuitView,
                           circuit,
                           position,
                           rotation,
                           properties);
  }

  @Override
  public Class<AndGateView> getViewClass()
  {
    return AndGateView.class;
  }
}

