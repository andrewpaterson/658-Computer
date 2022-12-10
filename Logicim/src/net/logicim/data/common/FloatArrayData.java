package net.logicim.data.common;

import net.logicim.data.ArrayData;

import java.util.Map;

public class FloatArrayData
    extends ArrayData
{
  public float[] array;

  public FloatArrayData()
  {
    super();
    array = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new float[length];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

