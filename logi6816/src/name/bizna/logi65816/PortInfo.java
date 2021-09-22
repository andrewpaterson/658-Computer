package name.bizna.logi65816;

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

