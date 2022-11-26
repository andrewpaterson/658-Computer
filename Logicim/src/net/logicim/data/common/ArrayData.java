package net.logicim.data.common;

import net.logicim.data.PrimitiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayData
    extends PrimitiveData
{
  public List list;

  public ArrayData()
  {
    list = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    String sizeString = fields.get("size");
    int size = Integer.parseInt(sizeString);
    list = new ArrayList<>(size);
    for (int i = 0; i < size; i++)
    {
      list.add(null);
    }
  }

  @Override
  public Object getObject()
  {
    return list;
  }
}

