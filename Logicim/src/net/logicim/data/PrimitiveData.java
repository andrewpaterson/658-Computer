package net.logicim.data;

import java.util.Map;

public abstract class PrimitiveData
    extends SaveData
{
  public abstract void load(Map<String, String> fields);
}

