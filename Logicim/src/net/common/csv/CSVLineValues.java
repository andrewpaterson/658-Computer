package net.common.csv;

import java.util.Arrays;

public class CSVLineValues
    extends BaseCSVLineValues
{
  protected char[] chars;

  public CSVLineValues()
  {
    super();
    chars = new char[64];
  }

  public CSVLineValues(CSVLineValues source, int reduce)
  {
    super(source, reduce);
    chars = Arrays.copyOf(source.chars, source.numberOfUnits());
  }

  public CSVLineValues cloneValues(int reduce)
  {
    return new CSVLineValues(this, reduce);
  }

  @Override
  protected String createString(int start, int end)
  {
    return new String(chars, start, end - start);
  }

  @Override
  protected int getUnitArrayLength()
  {
    return chars.length;
  }

  @Override
  public void appendLineFeed()
  {
    appendChar('\n');
  }

  public void appendChar(char character)
  {
    int index = getIndexToWriteToExpandingArrayIfNecessary();
    chars[index] = character;
  }

  @Override
  protected void expandArray()
  {
    chars = Arrays.copyOf(chars, getUnitArrayLength() + (getUnitArrayLength() >> 1));
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
    {
      return false;
    }

    if (obj instanceof CSVLineValues)
    {
      CSVLineValues other = (CSVLineValues) obj;
      int numCharacters = numberOfUnits();
      for (int i = 0; i < numCharacters; i++)
      {
        if (chars[i] != other.chars[i])
        {
          return false;
        }
      }
      return true;
    }
    else
    {
      return false;
    }
  }

  public int _totalIndices()
  {
    return indicies.length;
  }

  public int _totalCharacters()
  {
    return chars.length;
  }
}

