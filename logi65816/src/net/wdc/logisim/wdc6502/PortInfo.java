package net.wdc.logisim.wdc6502;

import static com.cburch.logisim.instance.Port.*;

public class PortInfo
{
  protected String name;
  protected String type;
  protected String exclusive;
  protected int bitWidth;

  private PortInfo(String name, String type, String exclusive, int bitWidth)
  {
    this.name = name;
    this.type = type;
    this.exclusive = exclusive;
    this.bitWidth = bitWidth;
  }

  public static PortInfo outputShared(String name, int pins)
  {
    return new PortInfo(name, OUTPUT, SHARED, pins);
  }

  public static PortInfo inoutShared(String name, int pins)
  {
    return new PortInfo(name, INOUT, SHARED, pins);
  }

  public static PortInfo outputExclusive(String name)
  {
    return new PortInfo(name, OUTPUT, EXCLUSIVE, 1);
  }

  public static PortInfo outputShared(String name)
  {
    return new PortInfo(name, OUTPUT, SHARED, 1);
  }

  public static PortInfo inputShared(String name)
  {
    return new PortInfo(name, INPUT, SHARED, 1);
  }

  public static PortInfo inoutShared(String name)
  {
    return new PortInfo(name, INOUT, SHARED, 1);
  }
}

