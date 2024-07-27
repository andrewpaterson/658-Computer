package net.common.util;

import java.util.*;

public abstract class CollectionUtil
{
  public static String join(Iterable iterable, String joinToken)
  {
    StringBuilder result = new StringBuilder();

    Iterator iterator = iterable.iterator();
    while (iterator.hasNext())
    {
      Object element = iterator.next();
      String elementAsString = (element != null) ? element.toString() : "null";
      result.append(elementAsString);

      if (iterator.hasNext())
      {
        result.append(joinToken);
      }
    }

    return result.toString();
  }

  public static <T> List<T> newList(T... element)
  {
    return toList(element);
  }

  public static <T> List<T> toList(T[] array)
  {
    List<T> result = new ArrayList<T>(array.length);

    Collections.addAll(result, array);

    return result;
  }

  public static <T> List<T> copy(Iterable<T>... iterables)
  {
    List<T> result = new ArrayList<T>();

    for (Iterable<T> iterable : iterables)
    {
      for (T each : iterable)
      {
        result.add(each);
      }
    }

    return result;
  }

  public static <T> Set<T> newSet(T... element)
  {
    return toSet(element);
  }

  public static <T> Set<T> toSet(T[] array)
  {
    Set<T> result = new HashSet<T>(array.length);

    Collections.addAll(result, array);

    return result;
  }
}

