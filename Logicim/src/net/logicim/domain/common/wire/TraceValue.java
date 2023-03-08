package net.logicim.domain.common.wire;

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
}

