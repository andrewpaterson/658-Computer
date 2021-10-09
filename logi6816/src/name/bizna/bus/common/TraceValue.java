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

  public static TraceValue getTraceValue(Collection<TraceValue> traceValues)
  {
    boolean high = false;
    boolean low = false;
    for (TraceValue traceValue : traceValues)
    {
      if (traceValue.isInvalid())
      {
        return traceValue;
      }
      else if (traceValue.isHigh())
      {
        high = true;
      }
      else if (traceValue.isLow())
      {
        low = true;
      }
    }

    if (high && low)
    {
      return HighAndLow;
    }
    else if (high)
    {
      return High;
    }
    else if (low)
    {
      return Low;
    }
    else
    {
      return Undefined;
    }
  }

  public static TraceValue fromBoolean(boolean value)
  {
    return value ? TraceValue.High : TraceValue.Low;
  }
}

