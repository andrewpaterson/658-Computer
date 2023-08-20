package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class OrGateViewFactory
    extends ViewFactory<OrGateView, LogicGateProperties>
{
  @Override
  public OrGateView create(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation)
  {
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
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
  public OrGateView create(CircuitEditor circuitEditor,
                           SubcircuitView subcircuitView,
                           Int2D position,
                           Rotation rotation,
                           LogicGateProperties properties)
  {
    return new OrGateView(subcircuitView,
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

