package net.logicim.ui.property;

import static net.common.util.StringUtil.javaNameToHumanReadable;

public class FieldNameHelper
{
  public static String calculateHumanReadableName(String fieldName)
  {
    int index = fieldName.indexOf('_');
    String name = fieldName;
    if (index != -1)
    {
      name = fieldName.substring(0, index);
    }
    name = javaNameToHumanReadable(name);
    return name;
  }
}
