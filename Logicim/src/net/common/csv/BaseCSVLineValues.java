package net.common.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseCSVLineValues
{
  protected int[] indicies;
  protected int numIndicies;

  public BaseCSVLineValues()
  {
    indicies = new int[16];
    indicies[0] = 0;
    numIndicies = 1;
  }

  public BaseCSVLineValues(BaseCSVLineValues source, int reduce)
  {
    indicies = Arrays.copyOf(source.indicies, source.numberOfStrings() - reduce);
    numIndicies = source.numberOfStrings() - reduce;
  }

  public int numberOfUnits()
  {
    return indicies[numIndicies - 1];
  }

  public void reset()
  {
    indicies[0] = 0;
    numIndicies = 1;
  }

  public int numberOfStrings()
  {
    return numIndicies;
  }

  public void mark()
  {
    if (numIndicies >= indicies.length)
    {
      indicies = Arrays.copyOf(indicies, indicies.length + (indicies.length >> 1));
    }
    indicies[numIndicies] = indicies[numIndicies - 1];
    numIndicies++;
  }

  public List<String> createStrings()
  {
    int numStrings = numberOfStrings();
    List<String> strings = new ArrayList<>(numStrings);
    for (int i = 0; i < numStrings; i++)
    {
      strings.add(createString(i));
    }
    return strings;
  }

  public String createString(int i)
  {
    if (i == 0)
    {
      return createString(0, indicies[0]);
    }
    else
    {
      return createString(indicies[i - 1], indicies[i]);
    }
  }

  protected int getIndexToWriteToExpandingArrayIfNecessary()
  {
    int index = indicies[numIndicies - 1];
    if (index >= getUnitArrayLength())
    {
      expandArray();
    }
    indicies[numIndicies - 1]++;
    return index;
  }

  public boolean equals(Object obj)
  {
    if (obj instanceof BaseCSVLineValues)
    {
      BaseCSVLineValues other = (BaseCSVLineValues) obj;
      if (other.numIndicies != numIndicies)
      {
        return false;
      }

      for (int i = 0; i < numIndicies; i++)
      {
        if (indicies[i] != other.indicies[i])
        {
          return false;
        }
      }
    }
    return false;
  }

  protected abstract void expandArray();

  protected abstract String createString(int start, int end);

  protected abstract int getUnitArrayLength();

  public abstract void appendLineFeed();
}

