package net.logicim.domain.common.trace;

import net.logicim.common.util.StringUtil;

public enum TraceValue
{
  Low,
  High,
  Unsettled,
  Undriven,
  Error;

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

  public boolean isConnected()
  {
    return this != Undriven;
  }

  public boolean isImpedance()
  {
    return this == Undriven;
  }

  public String toEnumString()
  {
    return StringUtil.toEnumString(this);
  }

  public static TraceValue getOutputValue(boolean value)
  {
    return value ? TraceValue.High : TraceValue.Low;
  }
}

