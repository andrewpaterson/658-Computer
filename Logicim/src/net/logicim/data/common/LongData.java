package net.logicim.data.common;

import java.util.Map;

public class LongData
    extends ObjectData
{
  public long x;

  public LongData()
  {
  }

  public LongData(long x)
  {
    this.x = x;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    x = Long.parseLong(fields.get("x"));
  }

  @Override
  public Object getObject()
  {
    return x;
  }
}

