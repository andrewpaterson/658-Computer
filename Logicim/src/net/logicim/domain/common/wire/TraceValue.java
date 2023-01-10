package net.logicim.domain.common.wire;

import net.logicim.common.util.StringUtil;

public enum TraceValue
{
  Low,
  High,
  Unsettled,
  Undriven;

  public boolean isHigh()
  {
    return this == High;
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
}

