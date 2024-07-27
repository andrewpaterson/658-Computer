package net.common.reflect;

import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumStore
{
  protected Map<String, Class<? extends Enum<?>>> enumClassNames;

  protected static EnumStore instance;

  public static EnumStore getInstance()
  {
    if (instance == null)
    {
      instance = new EnumStore();
    }
    return instance;
  }

  public EnumStore()
  {
    enumClassNames = new LinkedHashMap<>();

    Map<String, PackageInspector> packageInspectors = PackageInspectorStore.getInstance().getPackageInspectors();
    for (PackageInspector packageInspector : packageInspectors.values())
    {
      List<Class<?>> allClasses = packageInspector.getAllClasses();
      for (Class<?> aClass : allClasses)
      {
        if (Enum.class.isAssignableFrom(aClass))
        {
          String simpleName = aClass.getSimpleName();
          Class<? extends Enum<?>> enumClass = enumClassNames.get(simpleName);
          if (enumClass != null)
          {
            if (!enumClass.equals(aClass))
            {
              throw new SimulatorException("An enum named [%s] already exists.", simpleName);
            }
          }
          else
          {
            enumClassNames.put(simpleName, (Class<? extends Enum<?>>) aClass);
          }
        }
      }
    }
  }

  public Class<? extends Enum<?>> getEnum(String simpleName)
  {
    return enumClassNames.get(simpleName);
  }
}

