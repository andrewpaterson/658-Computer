package net.logicim.data.common;

import java.util.Map;

public class DoubleData
    extends ObjectData
{
  public double x;

  public DoubleData()
  {
  }

  public DoubleData(double x)
  {
    this.x = x;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    x = Double.parseDouble(fields.get("x"));

  }

  @Override
  public Object getObject()
  {
    return x;
  }
}

