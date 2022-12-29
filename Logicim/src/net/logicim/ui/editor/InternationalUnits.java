package net.logicim.ui.editor;

import net.logicim.common.SimulatorException;

public class InternationalUnits
{
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
    String modifier;
    switch (scale)
    {
      case 0:
        modifier = "";
        break;
      case 1:
        modifier = "K";
        break;
      case 2:
        modifier = "M";
        break;
      case 3:
        modifier = "G";
        break;
      case 4:
        modifier = "T";
        break;
      case 5:
        modifier = "P";
        break;
      default:
        throw new SimulatorException("Cannot get SI unit for [%s] digits.", scale * 3);
    }
    return modifier;
  }

  protected static String getLesserModifier(int scale)
  {
    String modifier;
    switch (scale)
    {
      case 0:
        modifier = "";
        break;
      case 1:
        modifier = "m";
        break;
      case 2:
        modifier = "u";
        break;
      case 3:
        modifier = "n";
        break;
      case 4:
        modifier = "p";
        break;
      case 5:
        modifier = "f";
        break;
      default:
        throw new SimulatorException("Cannot get SI unit for [%s] digits.", scale * 3);
    }
    return modifier;
  }
}

