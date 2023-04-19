package net.logicim.data.common;

public class ValueData
    extends SaveData
{
  public Object value;

  public ValueData()
  {
  }

  @Override
  public Object getObject()
  {
    return value;
  }
}

