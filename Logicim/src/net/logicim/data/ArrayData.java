package net.logicim.data;

import java.util.Map;

public abstract class ArrayData
    extends SaveData
{
  public int index;

  public ArrayData()
  {
    index = -1;
  }

  public abstract void load(Map<String, String> fields);
}

