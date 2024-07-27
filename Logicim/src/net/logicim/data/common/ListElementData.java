package net.logicim.data.common;

import net.common.SimulatorException;

import java.util.Map;

public class ListElementData
    extends SaveData
{
  protected int index;
  protected ValueData value;

  public ListElementData()
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
    if (saveData instanceof ValueData)
    {
      value = (ValueData) saveData;
    }
    else
    {
      throw new SimulatorException("Cannot set ListElementData with [%s].", saveData.getClass().getSimpleName());
    }
  }
}

