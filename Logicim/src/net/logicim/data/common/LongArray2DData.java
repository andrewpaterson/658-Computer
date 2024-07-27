package net.logicim.data.common;

import java.util.Map;

public class LongArray2DData
    extends ArrayData
{
  public long[][] array;

  public LongArray2DData()
  {
    array = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new long[length][];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

