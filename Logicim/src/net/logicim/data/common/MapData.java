package net.logicim.data.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MapData
    extends ObjectData
{
  public List keys;
  public List values;

  public MapData()
  {
    keys = null;
    values = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    String sizeString = fields.get("size");
    int size = Integer.parseInt(sizeString);
    keys = new ArrayList<>(size);
    values = new ArrayList<>(size);
    for (int i = 0; i < size; i++)
    {
      keys.add(null);
      values.add(null);
    }
  }

  @Override
  public Object getObject()
  {
    Map<Object, Object> map = createMap();
    for (int i = 0; i < keys.size(); i++)
    {
      Object key = keys.get(i);
      Object value = values.get(i);
      map.put(key, value);
    }
    return map;
  }

  protected abstract Map<Object, Object> createMap();

  public void set(Object object)
  {
    MapElementData mapElement = (MapElementData) object;
    keys.set(mapElement.index, mapElement.key.value);
    values.set(mapElement.index, mapElement.value.value);
  }
}

