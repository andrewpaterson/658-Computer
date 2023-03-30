package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public abstract class DecorativeData<T extends DecorativeView<?>>
    extends StaticData<T>
{
  public DecorativeData()
  {
  }

  public T createAndLoad(SubcircuitEditor subcircuitEditor,
                         TraceLoader traceLoader,
                         boolean fullLoad,
                         CircuitSimulation circuitSimulation)
  {
    T componentView = create(subcircuitEditor,
                             circuitSimulation.getCircuit(),
                             traceLoader,
                             fullLoad);
    componentView.enable(circuitSimulation.getSimulation());

    if (selected)
    {
      subcircuitEditor.select(componentView);
    }
    return componentView;
  }

  public DecorativeData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    super(name, position, rotation, null, null, selected);
  }

  protected abstract T create(SubcircuitEditor subcircuitEditor,
                              Circuit circuit,
                              TraceLoader traceLoader,
                              boolean fullLoad);
}

