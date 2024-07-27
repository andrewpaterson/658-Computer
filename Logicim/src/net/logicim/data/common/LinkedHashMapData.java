package net.logicim.data.common;

import java.util.LinkedHashMap;

public class LinkedHashMapData
    extends MapData
{

  public LinkedHashMapData()
  {
  }

  @Override
  protected LinkedHashMap<Object, Object> createMap()
  {
    return new LinkedHashMap<>();
  }
}

