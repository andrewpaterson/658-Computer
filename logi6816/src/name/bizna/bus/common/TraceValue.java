package name.bizna.bus.common;

import java.util.Collection;

public enum TraceValue
{
  High,
  Low,
  Undefined,
  Error,
  HighAndLow;

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
           this == TraceValue.Undefined;
  }

  public boolean isInvalid()
  {
    return this == TraceValue.Undefined ||
           this == TraceValue.Error;
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

  public boolean isUndefined()
  {
    return this == Undefined;
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
    else if (isUndefined())
    {
      return '.';
    }
    else if (isError())
    {
      return 'x';
    }
    return ' ';
  }
}

