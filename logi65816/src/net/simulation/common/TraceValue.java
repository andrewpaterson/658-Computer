package net.simulation.common;

import net.util.StringUtil;

public enum TraceValue
{
  High,
  Low,
  HighAndLow,

  Unsettled,
  NotConnected,
  Error;

  public boolean isValid()
  {
    return this == TraceValue.HighAndLow ||
           this == TraceValue.High ||
           this == TraceValue.Low;
  }

  public boolean isValidOrUndefined()
  {
    return this == TraceValue.HighAndLow ||
           this == TraceValue.High ||
           this == TraceValue.Low ||
           this == TraceValue.Unsettled;
  }

  public boolean isInvalid()
  {
    return this == TraceValue.Unsettled ||
           this == TraceValue.Error ||
           this == TraceValue.NotConnected;
  }

  public boolean isHigh()
  {
    return this == High;
  }

  public boolean isError()
  {
    return this == Error;
  }

  public boolean isLow()
  {
    return this == Low;
  }

  public boolean isUnsettled()
  {
    return this == Unsettled;
  }

  public static TraceValue fromBoolean(boolean value)
  {
    return value ? TraceValue.High : TraceValue.Low;
  }

  public char getStringValue()
  {
    if (isHigh())
    {
      return '1';
    }
    else if (isLow())
    {
      return '0';
    }
    else if (isUnsettled())
    {
      return '.';
    }
    else if (isError())
    {
      return 'E';
    }
    return ' ';
  }

  public boolean isConnected()
  {
    return this != NotConnected;
  }

  public boolean isNotConnected()
  {
    return this == NotConnected;
  }

  public String toEnumString()
  {
    return StringUtil.toEnumString(this);
  }
}

