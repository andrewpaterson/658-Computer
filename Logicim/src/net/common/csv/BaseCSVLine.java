package net.common.csv;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCSVLine
{
  public boolean lastEmpty;
  public BaseCSVLineValues values;

  public BaseCSVLine()
  {
    this.lastEmpty = true;
  }

  public void mark()
  {
    values.mark();
    lastEmpty = true;
  }

  public int numStrings()
  {
    if (!lastEmpty)
    {
      return values.numberOfStrings();
    }
    else
    {
      return values.numberOfStrings() - 1;
    }
  }

  public String createString(int i)
  {
    if (i < numStrings())
    {
      return values.createString(i);
    }
    else
    {
      throw new ArrayIndexOutOfBoundsException(i);
    }
  }

  public List<String> toStringArray()
  {
    if (!lastEmpty)
    {
      return values.createStrings();
    }
    else
    {
      int numStrings = numStrings();
      List<String> strings = new ArrayList<>(numStrings);
      for (int i = 0; i < numStrings; i++)
      {
        strings.add(createString(i));
      }
      return strings;
    }
  }

  public String toRowText()
  {
    return CSVUtil.toRowText(toStringArray());
  }

  public void reset()
  {
    lastEmpty = true;
    values.reset();
  }

  public boolean isLastEmpty()
  {
    return lastEmpty;
  }

  public void appendLineFeed()
  {
    values.appendLineFeed();
  }

  public abstract void append(int unit);

  public abstract BaseCSVLineValues cloneValues();
}

