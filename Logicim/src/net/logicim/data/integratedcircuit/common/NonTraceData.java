package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.ui.common.Rotation;

public class NonTraceData
    extends ReflectiveData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected boolean selected;

  public NonTraceData()
  {
  }

  public NonTraceData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    this.position = new Int2D(position);
    this.rotation = rotation;
    this.name = name;
    this.selected = selected;
  }
}

