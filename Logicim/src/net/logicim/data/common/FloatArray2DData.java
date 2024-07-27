package net.logicim.data.common;

import java.util.Map;

public class FloatArray2DData
    extends ArrayData
{
  public float[][] array;

  public FloatArray2DData()
  {
    array = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new float[length][];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

