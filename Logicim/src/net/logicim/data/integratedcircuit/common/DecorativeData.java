package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public abstract class DecorativeData<T extends DecorativeView<?>>
    extends StaticData<T>
{
  public DecorativeData()
  {
  }

  public T createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, boolean createConnections)
  {
    T componentView = create(circuitEditor, traceLoader);

    if (selected)
    {
      circuitEditor.select(componentView);
    }
    return componentView;
  }

  public DecorativeData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    super(name, position, rotation, null, null, selected);
  }

  protected abstract T create(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

