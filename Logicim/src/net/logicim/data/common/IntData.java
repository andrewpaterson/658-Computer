package net.logicim.data.common;

import java.util.Map;

public class IntData
    extends ObjectData
{
  public int x;

  public IntData()
  {
  }

  public IntData(int x)
  {
    this.x = x;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    x = Integer.parseInt(fields.get("x"));
  }

  @Override
  public Object getObject()
  {
    return x;
  }
}

