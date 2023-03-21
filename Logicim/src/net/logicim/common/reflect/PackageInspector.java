package net.logicim.common.reflect;

import net.logicim.common.SimulatorException;

import java.util.*;

public class PackageInspector
{
  private String packageName;
  private List<Class<?>> classes;
  private List<PackageInspector> subPackageInspectors;

  protected PackageInspector(String packageName)
  {
    this.packageName = packageName;
    classes = new ArrayList<>();
    subPackageInspectors = new ArrayList<>();
  }

  public void sortClasses()
  {
    classes.sort(new Comparator<>()
    {
      public int compare(Class<?> o1, Class<?> o2)
      {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }

  public List<Class<?>> getAllClasses()
  {
    return getSubClasses(Object.class);
  }

  private List<Class<?>> getSubclasses(Class<?> superClass, boolean immediateOnly)
  {
    List<Class<?>> allClasses = new ArrayList<>();

    for (PackageInspector packageInspector : getThisAndAllSubPackageInspectors())
    {
      for (Class<?> aClass : packageInspector.classes)
      {
        if (needsToAddClass(aClass, superClass, immediateOnly))
        {
          allClasses.add(aClass);
        }
      }
    }

    return allClasses;
  }

  private boolean needsToAddClass(Class<?> aClass, Class<?> superClass, boolean immediateOnly)
  {
    if (immediateOnly)
    {
      return aClass.getSuperclass() == superClass;
    }
    else
    {
      return superClass.isAssignableFrom(aClass) && (aClass != superClass);
    }
  }

  public <T> List<Class<T>> getInstantiableSubClasses(Class<T> superClass)
  {
    List<Class<T>> subClasses = (List) getSubClasses(superClass);

    List<Class<T>> result = new ArrayList<>();
    for (Class<T> aclass : subClasses)
    {
      ClassInspector classInspector = ClassInspector.forClass(aclass);
      if (!classInspector.isAbstract())
      {
        result.add(aclass);
      }
    }

    return (List<Class<T>>) Collections.<T>unmodifiableList((List<? extends T>) result);
  }

  public List<Class<?>> getSubClasses(Class<?> superClass)
  {
    return getSubclasses(superClass, false);
  }

  public List<Class<?>> getImmediateSubClasses(Class<?> superClass)
  {
    return getSubclasses(superClass, true);
  }

  public Class<?> findClassBySimpleName(String simpleName)
  {
    Collection<Class<?>> classes = findClassesBySimpleName(simpleName);
    if (classes.size() == 1)
    {
      return classes.iterator().next();
    }
    else
    {
      throw new SimulatorException("Could not find unique class named [%s].", simpleName);
    }
  }

  private Collection<Class<?>> findClassesBySimpleName(String simpleName)
  {
    Collection<Class<?>> results = new ArrayList<>();

    for (Class<?> aClass : classes)
    {
      if (aClass.getSimpleName().equals(simpleName))
      {
        results.add(aClass);
      }
    }

    for (PackageInspector packageInspector : subPackageInspectors)
    {
      results.addAll(packageInspector.findClassesBySimpleName(simpleName));
    }

    return results;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public Collection<PackageInspector> getThisAndAllSubPackageInspectors()
  {
    Collection<PackageInspector> result = new ArrayList<>(getAllSubPackageInspectors());
    result.add(this);
    return Collections.unmodifiableCollection(result);
  }

  public Collection<PackageInspector> getAllSubPackageInspectors()
  {
    Collection<PackageInspector> result = new ArrayList<>();
    addSubPackageInspectors(result);
    return Collections.unmodifiableCollection(result);

  }

  private void addSubPackageInspectors(Collection<PackageInspector> result)
  {
    for (PackageInspector packageInspector : subPackageInspectors)
    {
      result.add(packageInspector);
      packageInspector.addSubPackageInspectors(result);
    }
  }

  public String toString()
  {
    return String.format("PackageInspector(%s)", packageName);
  }

  public List<String> getSubpackageShortNames()
  {
    List<String> results = new ArrayList<String>();

    for (PackageInspector packageInspector : subPackageInspectors)
    {
      String subPackageName = packageInspector.getPackageName();
      results.add(subPackageName.substring(subPackageName.lastIndexOf('.') + 1));
    }

    return results;
  }

  protected void addClass(Class<?> aClass)
  {
    classes.add(aClass);
  }

  protected void addSubPackageInspectors(PackageInspector packageInspector)
  {
    subPackageInspectors.add(packageInspector);
  }

  public List<Class<?>> getPackageClasses()
  {
    return classes;
  }

  public void addClasses(List<Class<?>> packageClasses)
  {
    for (Class<?> packageClass : packageClasses)
    {
      if (!classes.contains(packageClass))
      {
        addClass(packageClass);
      }
    }
    sortClasses();
  }
}

