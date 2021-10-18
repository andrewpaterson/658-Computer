package net.wdc65xx.logisim;

import static com.cburch.logisim.instance.Port.*;

public class PortInfo
{
  protected int index;
  protected String lowName;
  protected String highName;
  protected String type;
  protected String exclusive;
  protected int bitWidth;

  private PortInfo(int index, String name, String type, String exclusive, int bitWidth)
  {
    this.index = index;
    this.lowName = name;
    this.highName = name;
    this.type = type;
    this.exclusive = exclusive;
    this.bitWidth = bitWidth;
  }

  public PortInfo setHighName(String highName)
  {
    this.highName = highName;
    return this;
  }

  public static PortInfo outputShared(int index, String name, int pins)
  {
    return new PortInfo(index, name, OUTPUT, SHARED, pins);
  }

  public static PortInfo inoutShared(int index, String name, int pins)
  {
    return new PortInfo(index, name, INOUT, SHARED, pins);
  }

  public static PortInfo outputShared(int index, String name)
  {
    return new PortInfo(index, name, OUTPUT, SHARED, 1);
  }

  public static PortInfo outputExclusive(int index, String name)
  {
    return new PortInfo(index, name, OUTPUT, EXCLUSIVE, 1);
  }

  public static PortInfo inputShared(int index, String name)
  {
    return new PortInfo(index, name, INPUT, SHARED, 1);
  }

  public static PortInfo inoutShared(int index, String name)
  {
    return new PortInfo(index, name, INOUT, SHARED, 1);
  }
}

