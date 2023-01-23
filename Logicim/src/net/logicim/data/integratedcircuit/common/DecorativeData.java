package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public abstract class DecorativeData
    extends StaticData
{
  public DecorativeData()
  {
  }

  public DecorativeData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    super(name, position, rotation, selected);
  }

  @Override
  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, StaticView<?> componentView)
  {
  }

  protected abstract DecorativeView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

