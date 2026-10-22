package net.logicim.data.common;

import java.util.HashMap;

public class HashMapData
    extends MapData
{
  public HashMapData()
  {
  }

  @Override
  protected HashMap<Object, Object> createMap()
  {
    return new HashMap<>();
  }
}

