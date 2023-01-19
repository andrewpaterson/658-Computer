package net.logicim.common.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageInspectorStore
{
  private Map<String, PackageInspector> packageInspectors;

  private static PackageInspectorStore instance = null;

  public static PackageInspectorStore getInstance()
  {
    if (instance == null)
    {
      instance = new PackageInspectorStore();
    }
    return instance;
  }

  public PackageInspectorStore()
  {
    packageInspectors = new ConcurrentHashMap<>();
  }

  public PackageInspector getPackageInspector(String packageName)
  {
    PackageInspector packageInspector = packageInspectors.get(packageName);
    if (packageInspector != null)
    {
      return packageInspector;
    }

    addPackageInspectors(new PackageInspectorFactory(packageName).createPackageInspectors());

    return packageInspectors.get(packageName);
  }

  private void addPackageInspectors(Map<String, PackageInspector> newPackageInspectors)
  {
    for (String packageName : newPackageInspectors.keySet())
    {
      PackageInspector newPackageInspector = newPackageInspectors.get(packageName);
      PackageInspector packageInspector = packageInspectors.get(packageName);
      if (packageInspector == null)
      {
        packageInspectors.put(packageName, newPackageInspector);
      }
      else
      {
        packageInspector.addClasses(newPackageInspector.getPackageClasses());
      }
    }
  }

  public Map<String, PackageInspector> getPackageInspectors()
  {
    return packageInspectors;
  }
}

