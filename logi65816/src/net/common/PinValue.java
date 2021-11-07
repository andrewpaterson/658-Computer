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

  public long getValue()
  {
    return isHigh() ? 1 : 0;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    if (isError())
    {
      builder.append("E");
    }
    if (isNotConnected())
    {
      builder.append("NC");
    }
    if (isUnknown())
    {
      builder.append("U");
    }
    builder.append("[");
    builder.append(getValue());
    builder.append("]");
    return builder.toString();
  }
}

