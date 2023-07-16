package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class XnorGateViewFactory
    extends ViewFactory<XnorGateView, LogicGateProperties>
{
  @Override
  public XnorGateView create(CircuitEditor circuitEditor,
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
  public XnorGateView create(CircuitEditor circuitEditor,
                             SubcircuitView subcircuitView,
                             Int2D position,
                             Rotation rotation,
                             LogicGateProperties properties)
  {
    return new XnorGateView(subcircuitView,
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

