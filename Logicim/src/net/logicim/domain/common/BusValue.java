package net.logicim.domain.common;

public class BusValue
{
  protected static final BusValue errorValue = new BusValue(true, false, false);
  protected static final BusValue unknownValue = new BusValue(false, true, false);
  protected static final BusValue notConnectedValue = new BusValue(false, false, true);

  public long value;
  public boolean unknown;
  public boolean notConnected;
  public boolean error;

  public BusValue(long value)
  {
    this.value = value;
    this.unknown = false;
    this.notConnected = false;
    this.error = false;
  }

  public BusValue(boolean error, boolean unknown, boolean notConnected)
  {
    this.value = 0;
    this.unknown = unknown;
    this.notConnected = notConnected;
    this.error = error;
  }

  public static BusValue error()
  {
    return errorValue;
  }

  public static BusValue unknown()
  {
    return unknownValue;
  }

  public static BusValue notConnected()
  {
    return notConnectedValue;
  }

  public long getValue()
  {
    return value;
  }

  public boolean isUnknown()
  {
    return unknown;
  }

  public boolean isNotConnected()
  {
    return notConnected;
  }

  public boolean isError()
  {
    return error;
  }

  public boolean isValid()
  {
    return !(unknown || notConnected || error);
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    if (error)
    {
      builder.append("E");
    }
    if (notConnected)
    {
      builder.append("NC");
    }
    if (unknown)
    {
      builder.append("U");
    }
    builder.append("[");
    builder.append(Long.toHexString(value));
    builder.append("]");
    return builder.toString();
  }
}

