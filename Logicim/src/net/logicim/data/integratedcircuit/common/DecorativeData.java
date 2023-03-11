package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.circuit.SubcircuitView;
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
                         boolean createConnections,
                         Simulation simulation,
                         Circuit circuit)
  {
    T componentView = create(subcircuitEditor.getSubcircuitView(), circuit, traceLoader);
    componentView.enable(simulation);

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

  protected abstract T create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader);
}

