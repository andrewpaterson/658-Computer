package net.logicim.data.common;

import java.util.Map;

public class FloatData
    extends ObjectData
{
  public float x;

  public FloatData()
  {
  }

  public FloatData(float x)
  {
    this.x = x;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    x = Float.parseFloat(fields.get("x"));

  }

  @Override
  public Object getObject()
  {
    return x;
  }
}

