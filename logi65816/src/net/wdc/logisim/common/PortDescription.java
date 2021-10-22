package net.wdc.logisim.common;

import static com.cburch.logisim.instance.Port.*;

public class PortDescription
{
  public int index;
  public String lowName;
  public String highName;
  public String tooltip;
  public String type;
  public String exclusive;
  public int bitWidth;

  private PortDescription(int index, String name, String type, String exclusive, int bitWidth)
  {
    this.index = index;
    this.lowName = name;
    this.highName = name;
    this.type = type;
    this.exclusive = exclusive;
    this.bitWidth = bitWidth;
    this.tooltip = name;
  }

  public PortDescription setHighName(String highName)
  {
    this.highName = highName;
    this.tooltip = tooltip + " / " + highName;
    return this;
  }

  public PortDescription setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
    return this;
  }

  public static PortDescription outputShared(int index, String name, int pins)
  {
    return new PortDescription(index, name, OUTPUT, SHARED, pins);
  }

  public static PortDescription inoutShared(int index, String name, int pins)
  {
    return new PortDescription(index, name, INOUT, SHARED, pins);
  }

  public static PortDescription outputShared(int index, String name)
  {
    return new PortDescription(index, name, OUTPUT, SHARED, 1);
  }

  public static PortDescription outputExclusive(int index, String name)
  {
    return new PortDescription(index, name, OUTPUT, EXCLUSIVE, 1);
  }

  public static PortDescription inputShared(int index, String name)
  {
    return new PortDescription(index, name, INPUT, SHARED, 1);
  }

  public static PortDescription inoutShared(int index, String name)
  {
    return new PortDescription(index, name, INOUT, SHARED, 1);
  }
}

