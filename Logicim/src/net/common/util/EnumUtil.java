package net.common.util;

import net.common.reflect.ClassInspector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtil
{
  public static List<Enum<?>> toList(Class<? extends Enum<?>> anEnum)
  {
    Enum<?>[] enums = (Enum<?>[]) ClassInspector.forClass(anEnum).invokeByTypes("values");
    return CollectionUtil.newList(enums);
  }

  public static Map<Enum<?>, String> toEnumMap(List<? extends Enum<?>> enums)
  {
    Map<Enum<?>, String> result = new LinkedHashMap<>();
    for (Enum<?> element : enums)
    {
      result.put(element, StringUtil.toEnumString(element));
    }
    return result;
  }

  public static Enum<?> getEnum(Class<? extends Enum<?>> anEnum, String enumName)
  {
    List<Enum<?>> enums = toList(anEnum);
    for (Enum<?> element : enums)
    {
      String elementName = StringUtil.toEnumString(element);
      if (elementName.equals(enumName))
      {
        return element;
      }
    }
    return null;
  }
}

