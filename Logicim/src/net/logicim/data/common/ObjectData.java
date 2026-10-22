package net.logicim.data.common;

import java.util.Map;

public abstract class ObjectData
    extends ReflectiveData
{
  public abstract void load(Map<String, String> fields);
}

