package net.logicim.data.editor;

import net.logicim.common.type.Float2D;
import net.logicim.data.ReflectiveData;

public class SubcircuitParameterData
    extends ReflectiveData
{
  public String subcircuitTypeName;
  public Float2D position;
  public float zoom;

  public SubcircuitParameterData()
  {
  }

  public SubcircuitParameterData(String subcircuitTypeName, Float2D position, float zoom)
  {
    this.subcircuitTypeName = subcircuitTypeName;
    this.position = position;
    this.zoom = zoom;
  }
}

