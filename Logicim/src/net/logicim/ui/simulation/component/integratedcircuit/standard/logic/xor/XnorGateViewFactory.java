package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateProperties;

public class XnorGateViewFactory
    extends ViewFactory<XnorGateView, LogicGateProperties>
{
  @Override
  public XnorGateView create(CircuitEditor circuitEditor,
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
  public XnorGateView create(CircuitEditor circuitEditor,
                             Int2D position,
                             Rotation rotation,
                             LogicGateProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    return new XnorGateView(subcircuitView,
                            circuit,
                            position,
                            rotation,
                            properties);
  }

  @Override
  public Class<XnorGateView> getViewClass()
  {
    return XnorGateView.class;
  }
}

