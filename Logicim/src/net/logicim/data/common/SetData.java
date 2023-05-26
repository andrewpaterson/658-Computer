package net.logicim.data.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SetData
    extends ObjectData
{
  public List values;

  public SetData()
  {
    values = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    String sizeString = fields.get("size");
    int size = Integer.parseInt(sizeString);
    values = new ArrayList<>(size);
    for (int i = 0; i < size; i++)
    {
      values.add(null);
    }
  }

  @Override
  public Object getObject()
  {
    Set<Object> set = createSet();
    for (int i = 0; i < values.size(); i++)
    {
      Object value = values.get(i);
      set.add(value);
    }
    return set;
  }

  public void set(Object object)
  {
    SetElementData setElement = (SetElementData) object;
    values.set(setElement.index, setElement.value.object);
  }

  protected abstract Set<Object> createSet();
}

