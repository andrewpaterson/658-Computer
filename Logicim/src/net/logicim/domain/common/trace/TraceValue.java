package net.logicim.domain.common.trace;

import net.logicim.common.util.StringUtil;

public enum TraceValue
{
  Low,
  High,
  Unsettled,
  NotConnected,
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

