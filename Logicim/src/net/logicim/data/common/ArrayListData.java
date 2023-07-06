package net.logicim.data.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayListData
    extends ObjectData
{
  public List list;

  public ArrayListData()
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

  public void set(Object object)
  {
    ListElementData setElement = (ListElementData) object;
    list.set(setElement.index, setElement.value.object);
  }
}

