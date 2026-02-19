package net.logicim.ui.simulation.component.integratedcircuit.standard.probe;

import net.common.type.Int2D;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class ProbeViewFactory
    extends ViewFactory<ProbeView, ProbeProperties>
{
  @Override
  public ProbeView create(CircuitEditor circuitEditor,
                          Int2D position,
                          Rotation rotation)
  {
    return create(circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
                  rotation,
                  createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public ProbeProperties createInitialProperties()
  {
    return new ProbeProperties("", DefaultFamily.get(), 32, Radix.HEXADECIMAL);
  }

  @Override
  public ProbeView create(CircuitEditor circuitEditor,
                          SubcircuitView subcircuitView,
                          Int2D position,
                          Rotation rotation,
                          ProbeProperties properties)
  {
    return new ProbeView(subcircuitView,
                         position,
                         rotation,
                         properties);
  }

  @Override
  public Class<ProbeView> getViewClass()
  {
    return ProbeView.class;
  }
}

