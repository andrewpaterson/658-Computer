package net.logicim.data.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.EnumStore;
import net.logicim.common.util.EnumUtil;

import java.util.Map;

public class EnumData
    extends ObjectData
{
  public Enum<?> anEnum;

  public EnumData()
  {
    anEnum = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    String simpleClassName = fields.get("class");
    String enumName = fields.get("enum");

    Class<? extends Enum<?>> aClass = EnumStore.getInstance().getEnum(simpleClassName);
    if (aClass == null)
    {
      throw new SimulatorException("Enum class [%s] is not in the EnumStore.  Ensure it is in a package reachable by SaveDataClassStore.", simpleClassName);
    }
    this.anEnum = EnumUtil.getEnum(aClass, enumName);
  }

  @Override
  public Object getObject()
  {
    return anEnum;
  }
}

