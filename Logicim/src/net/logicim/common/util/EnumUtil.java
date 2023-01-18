package net.logicim.common.util;

import net.logicim.common.reflect.ClassInspector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtil
{
  public static List<Enum<?>> toList(Class<? extends Enum<?>> aClass)
  {
    Enum<?>[] enums = (Enum<?>[]) ClassInspector.forClass(aClass).invokeByTypes("values");
    return CollectionUtil.newList(enums);
  }

  public static Map<Enum<?>, String> toEnumMap(List<? extends Enum<?>> enums)
  {
    Map<Enum<?>, String> result = new LinkedHashMap<>();
    for (Enum<?> enumElement : enums)
    {
      result.put(enumElement, StringUtil.toEnumString(enumElement));
    }
    return result;
  }
}

