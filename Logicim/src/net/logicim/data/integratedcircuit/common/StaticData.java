package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;

public abstract class StaticData
    extends ReflectiveData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    this.name = name;
    this.position = position;
    this.rotation = rotation;
    this.selected = selected;
  }

  public abstract void createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

