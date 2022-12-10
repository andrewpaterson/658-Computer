package net.logicim.data;

import java.util.Map;

public abstract class ObjectData
    extends SaveData
{
  public abstract void load(Map<String, String> fields);
}

