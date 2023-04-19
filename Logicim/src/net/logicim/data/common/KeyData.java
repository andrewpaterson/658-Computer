package net.logicim.data.common;

public class KeyData
    extends SaveData
{
  public Object value;

  public KeyData()
  {
  }

  @Override
  public Object getObject()
  {
    return value;
  }
}

