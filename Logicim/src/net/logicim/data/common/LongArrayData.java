package net.logicim.data.common;

import net.logicim.data.ArrayData;

import java.util.Map;

public class LongArrayData
    extends ArrayData
{
  public long[] array;

  public LongArrayData()
  {
    array = null;
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
