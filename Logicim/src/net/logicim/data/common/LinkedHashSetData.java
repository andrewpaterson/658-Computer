package net.logicim.data.common;

import java.util.LinkedHashSet;

public class LinkedHashSetData
    extends SetData
{

  public LinkedHashSetData()
  {
  }

  @Override
  protected LinkedHashSet<Object> createSet()
  {
    return new LinkedHashSet<>();
  }
}

