package net.logicim.data;

import net.logicim.common.reflect.PackageInspector;
import net.logicim.common.reflect.PackageInspectorStore;
import net.logicim.data.common.SaveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDataClassStore
{
  private static SaveDataClassStore instance = null;

  protected Map<String, Class<SaveData>> classMap;

  public static SaveDataClassStore getInstance()
  {
    if (instance == null)
    {
      instance = new SaveDataClassStore();
    }
    return instance;
  }

  public SaveDataClassStore()
  {
    classMap = new HashMap<>();
    createPackageInspectors("net.logicim.data");
    createPackageInspectors("net.logicim.domain.integratedcircuit");
  }

  protected void createPackageInspectors(String packageName)
  {
    PackageInspector packageInspector = PackageInspectorStore.getInstance().getPackageInspector(packageName);
    List<Class<SaveData>> instantiableSubClasses = packageInspector.getInstantiableSubClasses(SaveData.class);
    for (Class<SaveData> instantiableSubClass : instantiableSubClasses)
    {
      classMap.put(instantiableSubClass.getSimpleName(), instantiableSubClass);
    }
  }

  public Class<SaveData> getClass(String simpleClassName)
  {
    return classMap.get(simpleClassName);
  }

  public List<Class<SaveData>> findAll()
  {
    return new ArrayList<>(classMap.values());
  }
}

