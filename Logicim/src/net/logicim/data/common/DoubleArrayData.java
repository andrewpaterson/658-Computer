package net.logicim.data.common;

import java.util.Map;

public class DoubleArrayData
    extends ArrayData
{
  public double[] array;

  public DoubleArrayData()
  {
    super();
    array = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int length = Integer.parseInt(fields.get("length"));
    array = new double[length];
    index = 0;
  }

  @Override
  public Object getObject()
  {
    return array;
  }
}

