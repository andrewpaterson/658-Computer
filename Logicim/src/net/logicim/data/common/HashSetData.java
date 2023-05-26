package net.logicim.data.common;

import java.util.HashSet;

public class HashSetData
    extends SetData
{

  public HashSetData()
  {
  }

  @Override
  protected HashSet<Object> createSet()
  {
    return new HashSet<>();
  }
}

