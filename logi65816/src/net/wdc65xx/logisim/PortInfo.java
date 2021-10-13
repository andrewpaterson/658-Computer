package net.wdc65xx.logisim;

import com.cburch.logisim.instance.Port;

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

  public static PortInfo sharedOutput(String name, int pins)
  {
    return new PortInfo(name, Port.OUTPUT, Port.SHARED, pins);
  }

  public static PortInfo sharedBidirectional(String name, int pins)
  {
    return new PortInfo(name, Port.INOUT, Port.SHARED, pins);
  }

  public static PortInfo sharedOutput(String name)
  {
    return new PortInfo(name, Port.OUTPUT, Port.SHARED, 1);
  }

  public static PortInfo exclusiveOutput(String name)
  {
    return new PortInfo(name, Port.OUTPUT, Port.EXCLUSIVE, 1);
  }

  public static PortInfo sharedInput(String name)
  {
    return new PortInfo(name, Port.INPUT, Port.SHARED, 1);
  }

  public static PortInfo sharedBidirectional(String name)
  {
    return new PortInfo(name, Port.INOUT, Port.SHARED, 1);
  }
}

