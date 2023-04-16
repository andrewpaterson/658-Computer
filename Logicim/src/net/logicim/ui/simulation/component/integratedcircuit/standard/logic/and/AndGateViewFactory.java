package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

public class AndGateViewFactory
    extends ViewFactory<AndGateView, LogicGateProperties>
{
  @Override
  public AndGateView create(CircuitEditor circuitEditor,
                            Int2D position,
                            Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  createDefaultProperties(getViewClass()));
  }

  @Override
  public LogicGateProperties createInitialProperties()
  {
    return new LogicGateProperties("",
                                   DefaultFamily.get(),
                                   false,
                                   2,
                                   1);
  }

  @Override
  public AndGateView create(CircuitEditor circuitEditor,
                            Int2D position,
                            Rotation rotation,
                            LogicGateProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    return new AndGateView(subcircuitView,
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

