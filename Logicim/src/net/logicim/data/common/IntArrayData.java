package net.logicim.data.common;

import java.util.Map;

public class IntArrayData
    extends ArrayData
{
  public int[] array;

  public IntArrayData()
  {
    array = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new int[length];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

