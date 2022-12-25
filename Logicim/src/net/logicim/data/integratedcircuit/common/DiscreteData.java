package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;

public abstract class DiscreteData
    extends ReflectiveData
{
  protected Int2D position;
  protected Rotation rotation;
  protected String name;

  public DiscreteData()
  {
  }

  public DiscreteData(Int2D position, Rotation rotation, String name)
  {
    this.position = position;
    this.rotation = rotation;
    this.name = name;
  }

  public abstract DiscreteView createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

