package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.defaults.DefaultFamily;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class InverterViewFactory
    extends ViewFactory<InverterView, BufferProperties>
{
  @Override
  public InverterView create(CircuitEditor circuitEditor,
                             Int2D position,
                             Rotation rotation)
  {
    return create(circuitEditor,
                  position,
                  rotation,
                  createDefaultProperties(getViewClass()));
  }

  @Override
  public BufferProperties createInitialProperties()
  {
    return new BufferProperties("",
                                DefaultFamily.get(),
                                true,
                                1,
                                1);
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor,
                             Int2D position,
                             Rotation rotation,
                             BufferProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    return new InverterView(subcircuitView,
                            circuit,
                            position,
                            rotation,
                            properties);
  }

  @Override
  public Class<InverterView> getViewClass()
  {
    return InverterView.class;
  }
}

