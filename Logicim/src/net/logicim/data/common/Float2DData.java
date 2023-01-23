package net.logicim.data.common;

import net.logicim.common.type.Float2D;
import net.logicim.data.ObjectData;

import java.util.Map;

public class Float2DData
    extends ObjectData
{
  public Float2D float2D;

  public Float2DData()
  {
    float2D = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    float x = Float.parseFloat(fields.get("x"));
    float y = Float.parseFloat(fields.get("y"));
    float2D = new Float2D(x, y);
  }

  @Override
  public Object getObject()
  {
    return float2D;
  }
}

