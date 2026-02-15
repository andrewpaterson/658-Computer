package net.logicim.ui.simulation.component.integratedcircuit.standard.counter;

import net.common.type.Int2D;
import net.logicim.domain.common.defaults.DefaultFamily;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class CounterViewFactory
    extends ViewFactory<CounterView, CounterProperties>
{
  @Override
  public CounterView create(CircuitEditor circuitEditor,
                            Int2D position,
                            Rotation rotation)
  {
    return create(circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
                  rotation,
                  createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(),
                                          getViewClass(),
                                          true));
  }

  @Override
  public CounterProperties createInitialProperties()
  {
    return new CounterProperties("",
                                 DefaultFamily.get(),
                                 false,
                                 4,
                                 0xf);
  }

  @Override
  public CounterView create(CircuitEditor circuitEditor,
                            SubcircuitView subcircuitView,
                            Int2D position,
                            Rotation rotation,
                            CounterProperties properties)
  {
    return new CounterView(subcircuitView,
                           position,
                           rotation,
                           properties);
  }

  @Override
  public Class<CounterView> getViewClass()
  {
    return CounterView.class;
  }
}

