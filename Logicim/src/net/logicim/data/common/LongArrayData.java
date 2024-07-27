package net.logicim.data.common;

import java.util.Map;

public class LongArrayData
    extends ArrayData
{
  public long[] array;

  public LongArrayData()
  {
    array = null;
  }

  public LongArrayData(long[] array)
  {
    this.array = array;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new long[length];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

