package net.common;

public enum PinValue
{
  High,
  Low,
  Unknown,
  NotConnected,
  Error;

  public boolean isError()
  {
    return this == Error;
  }

  public boolean isUnknown()
  {
    return this == Unknown;
  }

  public boolean isHigh()
  {
    return this == High;
  }

  public boolean isLow()
  {
    return this == Low;
  }

  public boolean isNotConnected()
  {
    return this == NotConnected;
  }
}

