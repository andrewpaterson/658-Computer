package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
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
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public BufferProperties createInitialProperties()
  {
    return new BufferProperties("",
                                DefaultFamily.get(),
                                false,
                                1,
                                1);
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor,
                             SubcircuitView subcircuitView,
                             Int2D position,
                             Rotation rotation,
                             BufferProperties properties)
  {
    return new InverterView(subcircuitView,
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

