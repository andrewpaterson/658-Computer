package net.logicim.ui.editor;

import net.common.SimulatorException;

public class InternationalUnits
{
  public static char[] greaterPrefixes = {'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y'};
  public static char[] lesserPrefixes = {'m', 'u', 'n', 'p', 'f', 'a', 'z', 'y'};

  public static String toString(double f, String unit)
  {
    if (f > 0)
    {
      if (f >= 1)
      {
        return greaterThanOrEqualOne(f, unit);
      }
      else
      {
        return lessThanOne(f, unit);
      }
    }
    else if (f == 0)
    {
      return "0.00 " + unit;
    }
    else
    {
      f = Math.abs(f);
      if (f >= 1)
      {
        return "-" + greaterThanOrEqualOne(f, unit);
      }
      else
      {
        return "-" + lessThanOne(f, unit);
      }
    }
  }

  protected static String greaterThanOrEqualOne(double f, String unit)
  {
    double log = Math.log10(f);
    int digits = (int) (Math.floor(log));
    int remainder = digits % 3;
    int scale = (digits / 3);

    double divisor = Math.pow(10, digits);
    int value = 0;
    for (int i = 0; i < 4; i++)
    {
      value *= 10;
      long v = (int) (f / divisor);
      value += v;
      f -= v * divisor;
      divisor /= 10;
    }
    if (value % 10 >= 5)
    {
      value = value / 10 + 1;
    }
    else
    {
      value = value / 10;
    }

    String wholeNumber = Integer.toString(value);
    StringBuilder builder = new StringBuilder(wholeNumber);
    if (remainder < 2)
    {
      builder.insert(remainder + 1, '.');
    }

    String modifier = getGreaterModifier(scale);

    return builder.toString() + " " + modifier + unit;
  }

  protected static String lessThanOne(double f, String unit)
  {
    double log = Math.log10(f);
    int digits = (int) (Math.floor(log));
    int remainder = 2 + ((digits + 1) % 3);
    int scale = ((-digits + 2) / 3);

    double divisor = Math.pow(10, digits);
    int value = 0;
    for (int i = 0; i < 4; i++)
    {
      value *= 10;
      long v = (int) (f / divisor);
      value += v;
      f -= v * divisor;
      divisor /= 10;
    }
    if (value % 10 >= 5)
    {
      value = value / 10 + 1;
    }
    else
    {
      value = value / 10;
    }

    String wholeNumber = Integer.toString(value);

    StringBuilder builder = new StringBuilder(wholeNumber);
    if (remainder < 2)
    {
      builder.insert(remainder + 1, '.');
    }

    wholeNumber = builder.toString();

    String modifier = getLesserModifier(scale);

    return wholeNumber + " " + modifier + unit;
  }

  protected static String getGreaterModifier(int scale)
  {
    if (scale == 0)
    {
      return "";
    }
    else
    {
      int index = scale - 1;
      if (index >= 0 && index < greaterPrefixes.length)
      {
        return "" + greaterPrefixes[scale - 1];
      }
      else
      {
        throw new SimulatorException("Cannot get SI magnitude for [%s] digits.", scale * 3);
      }
    }
  }

  protected static String getLesserModifier(int scale)
  {
    if (scale == 0)
    {
      return "";
    }
    else
    {
      int index = scale - 1;
      if (index >= 0 && index < lesserPrefixes.length)
      {
        return "" + lesserPrefixes[scale - 1];
      }
      else
      {
        throw new SimulatorException("Cannot get SI magnitude for [-%s] digits.", scale * 3);
      }
    }
  }

  public static double parse(String s, String unit)
  {
    s = s.trim();
    char prefix = ' ';
    if (!unit.isEmpty())
    {
      int index = s.toLowerCase().lastIndexOf(unit.toLowerCase());
      if (index != -1)
      {
        prefix = s.charAt(index - 1);
        s = s.substring(0, index - 1);
        s = s.trim();
      }
    }
    double value = Double.parseDouble(s);
    int magnitude = getPrefixMagnitude(prefix);
    double multiplier = Math.pow(10, magnitude);
    return value * multiplier;
  }

  protected static int getPrefixMagnitude(char prefix)
  {
    if (prefix != ' ')
    {
      for (int i = 0; i < greaterPrefixes.length; i++)
      {
        char greaterPrefix = greaterPrefixes[i];
        if (prefix == greaterPrefix)
        {
          return (i + 1) * 3;
        }
      }

      for (int i = 0; i < lesserPrefixes.length; i++)
      {
        char lesserPrefix = lesserPrefixes[i];
        if (prefix == lesserPrefix)
        {
          return (-i - 1) * 3;
        }
      }

      throw new SimulatorException("Cannot get SI magnitude for prefix [%s].", prefix);
    }
    return 0;
  }
}

