package net.common.reflect;

import net.common.SimulatorException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PackageInspectorFactory
{
  private static final String JAVA_CLASS_SUFFIX = ".class";

  private List<String> packageList;

  public PackageInspectorFactory(String... packages)
  {
    packageList = Arrays.asList(packages);
  }

  public Map<String, PackageInspector> createPackageInspectors()
  {
    List<Class<?>> classes = getClasses();
    Map<String, PackageInspector> classInspectors = createClassPackageInspectors(classes);
    return createIntermediatePackageInspectors(classInspectors);
  }

  private Map<String, List<URL>> forPackages()
  {
    Map<String, List<URL>> allResources = new LinkedHashMap<>();
    for (String aPackage : packageList)
    {
      allResources.put(aPackage, getResourceURLs(aPackage));
    }
    return allResources;
  }

  private List<URL> getResourceURLs(String packageName)
  {
    List<URL> urlResources = new ArrayList<>();
    try
    {
      Enumeration<URL> resources = getClassLoader().getResources(packageName.replace('.', '/'));
      while (resources.hasMoreElements())
      {
        URL url = resources.nextElement();
        urlResources.add(url);
      }
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    return urlResources;
  }

  private void setSubPackageInspectors(Map<String, PackageInspector> allInspectors)
  {
    for (PackageInspector packageInspector : allInspectors.values())
    {
      String packageName = packageInspector.getPackageName();
      int index = packageName.lastIndexOf('.');
      if (index != -1)
      {
        String superPackageName = packageName.substring(0, index);
        PackageInspector superPackageInspector = allInspectors.get(superPackageName);
        if (superPackageInspector != null)
        {
          superPackageInspector.addSubPackageInspectors(packageInspector);
        }
      }
    }
  }

  private Map<String, PackageInspector> createIntermediatePackageInspectors(Map<String, PackageInspector> inspectors)
  {
    Collection<PackageInspector> values = inspectors.values();

    Map<String, PackageInspector> map = new LinkedHashMap<>(inspectors);
    for (PackageInspector packageInspector : values)
    {
      String packageName = packageInspector.getPackageName();
      for (; ; )
      {
        if (packageList.contains(packageName))
        {
          break;
        }

        int index = packageName.lastIndexOf('.');
        String superPackageName = packageName.substring(0, index);
        PackageInspector superPackageInspector = map.get(superPackageName);
        if (superPackageInspector == null)
        {
          superPackageInspector = new PackageInspector(superPackageName);
          map.put(superPackageName, superPackageInspector);
        }
        else
        {
          break;
        }
        packageName = superPackageName;
      }
    }

    setSubPackageInspectors(map);
    return map;
  }

  private Map<String, PackageInspector> createClassPackageInspectors(List<Class<?>> classes)
  {
    Map<String, PackageInspector> classInspectors = new HashMap<>();

    for (Class<?> aClass : classes)
    {
      String name = aClass.getPackage().getName();
      PackageInspector packageInspector = classInspectors.get(name);
      if (packageInspector == null)
      {
        packageInspector = new PackageInspector(name);
        classInspectors.put(name, packageInspector);
      }
      packageInspector.addClass(aClass);
    }

    for (PackageInspector packageInspector : classInspectors.values())
    {
      packageInspector.sortClasses();
    }

    return classInspectors;
  }

  private List<Class<?>> getClasses()
  {
    Map<String, List<URL>> resourceURLs = forPackages();

    List<Class<?>> classes = new ArrayList<>();
    for (String packageName : resourceURLs.keySet())
    {
      List<URL> urls = resourceURLs.get(packageName);
      for (URL url : urls)
      {
        classes.addAll(getPackageClasses(url, packageName));
      }
    }
    return classes;
  }

  private List<Class<?>> getPackageClasses(URL resource, String packageName)
  {
    String protocol = resource.getProtocol();
    if (protocol.equals("file"))
    {
      return getClassesInDirectory(resource, packageName);
    }
    throw new SimulatorException("Could not get resource named [%s]", resource.toString());
  }

  private Class<?> getClassFromName(URL resource, String classFilename)
  {
    try
    {
      return getClassLoader().loadClass(classFilename);
    }
    catch (Throwable e)
    {
      String message = e.getMessage();
      if (message.startsWith("Failed to link") || e instanceof NoClassDefFoundError)
      {
        return null;
      }
      else
      {
        throw new SimulatorException(resource + " -> " + classFilename + " : " + e.getClass().getSimpleName() + " " + message);
      }
    }
  }

  private ClassLoader getClassLoader()
  {
    return Thread.currentThread().getContextClassLoader();
  }

  private boolean isValidClassFile(File file)
  {
    String name = file.getName();
    return (file.isFile() && (isValidClassName(name)));
  }

  private boolean isValidClassName(String name)
  {
    return name.endsWith(JAVA_CLASS_SUFFIX);
  }

  private List<Class<?>> getClassesInDirectory(URL resource, String packageName)
  {
    File file = new File(resource.getFile());
    if (file.exists())
    {
      return getClassesInDirectory(resource, file, packageName);
    }
    return new ArrayList<>();
  }

  private List<Class<?>> getClassesInDirectory(URL resource, File file, String packageName)
  {
    File[] files = file.listFiles();
    if (files != null)
    {
      List<Class<?>> classes = new ArrayList<>();
      for (File childFile : files)
      {
        if (isValidClassFile(childFile))
        {
          String path = childFile.getPath();
          String directoryName = resource.getPath();
          String classFileName = path.substring(directoryName.length());
          classFileName = stripClassSuffix(classFileName);
          classFileName = classFileName.replace('/', '.');
          classFileName = classFileName.replace('\\', '.');
          if (!packageName.endsWith("."))
          {
            packageName = packageName + ".";
          }
          classFileName = packageName + classFileName;
          if (isIncluded(classFileName))
          {
            Class<?> aClass = getClassFromName(resource, classFileName);
            if (aClass != null)
            {
              classes.add(aClass);
            }
          }
        }
        else if (childFile.isDirectory())
        {
          classes.addAll(getClassesInDirectory(resource, childFile, packageName));
        }
      }
      return classes;
    }
    return new ArrayList<>();
  }

  private boolean isIncluded(String name)
  {
    for (String s : packageList)
    {
      if (name.startsWith(s))
      {
        return true;
      }
    }
    return false;
  }

  private String stripClassSuffix(String classFilename)
  {
    String className;
    int indexOfSuffix = classFilename.length() - JAVA_CLASS_SUFFIX.length();
    className = classFilename.substring(0, indexOfSuffix);
    return className;
  }

  private String getAvailablePackageInspectors(Map<String, PackageInspector> map)
  {
    StringBuilder builder = new StringBuilder();
    List<String> packages = new ArrayList<>();
    for (String aPackage : map.keySet())
    {
      packages.add(aPackage);
    }
    Collections.sort(packages);
    for (String aPackage : packages)
    {
      builder.append(aPackage).append("\n");
    }
    return builder.toString();
  }

  private String getAvailableRootURLs(Map<String, List<URL>> resourceURLs)
  {
    StringBuilder builder = new StringBuilder();
    for (String s : resourceURLs.keySet())
    {
      List<URL> urls = resourceURLs.get(s);
      for (URL url : urls)
      {
        builder.append(s).append(" -> ").append(url.toString()).append("\n");
      }
    }
    return builder.toString();
  }
}

