package net.logicim.data.common;

import net.common.SimulatorException;

import java.util.Map;

public class MapElementData
    extends SaveData
{
  protected int index;
  protected KeyData key;
  protected ValueData value;

  public MapElementData()
  {
    index = -1;
  }

  @Override
  public Object getObject()
  {
    return this;
  }

  public void load(Map<String, String> fields)
  {
    index = Integer.parseInt(fields.get("index"));
  }

  public void set(SaveData saveData)
  {
    if (saveData instanceof KeyData)
    {
      key = (KeyData) saveData;
    }
    else if (saveData instanceof ValueData)
    {
      value = (ValueData) saveData;
    }
    else
    {
      throw new SimulatorException("Cannot set MapElementData with [%s].", saveData.getClass().getSimpleName());
    }
  }
}

