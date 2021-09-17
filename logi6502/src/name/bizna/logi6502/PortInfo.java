package name.bizna.logi6502;

public class PortInfo
{
  public final String name, type, exclusive;
  public final int bitWidth;

  private PortInfo(String name, String type, String exclusive, int bitWidth)
  {
    this.name = name;
    this.type = type;
    this.exclusive = exclusive;
    this.bitWidth = bitWidth;
  }

  public static PortInfo sharedOutput(String name, int pins)
  {
    return new PortInfo(name, "output", "shared", pins);
  }

  public static PortInfo sharedBidirectional(String name, int pins)
  {
    return new PortInfo(name, "inout", "shared", pins);
  }

  public static PortInfo exclusiveOutput(String name)
  {
    return new PortInfo(name, "output", "exclusive", 1);
  }

  public static PortInfo sharedOutput(String name)
  {
    return new PortInfo(name, "output", "shared", 1);
  }

  public static PortInfo sharedInput(String name)
  {
    return new PortInfo(name, "input", "shared", 1);
  }

  public static PortInfo sharedBidirectional(String name)
  {
    return new PortInfo(name, "inout", "shared", 1);
  }
}

