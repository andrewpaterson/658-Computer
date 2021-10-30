package net.logisim.common;

public class PortDescription
{
  private final int index;

  private String lowName;
  private String highName;
  private String tooltip;
  private String type;
  private String exclusive;
  private int bitWidth;
  private boolean notBlank;
  private boolean inverting;
  private PortPosition position;

  private int offset;

  protected PortDescription(int index,
                            String name,
                            String type,
                            String exclusive,
                            int bitWidth,
                            boolean notBlank,
                            PortPosition position)
  {
    this.index = index;
    this.lowName = name;
    this.highName = name;
    this.type = type;
    this.exclusive = exclusive;
    this.bitWidth = bitWidth;
    this.tooltip = name;
    this.notBlank = notBlank;
    this.position = position;
    this.offset = 0;
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

  public PortDescription setInverting(boolean inverting)
  {
    this.inverting = inverting;
    return this;
  }

  public int index()
  {
    return index;
  }

  public boolean notBlank()
  {
    return notBlank;
  }

  public String getLowName()
  {
    return lowName;
  }

  public String getHighName()
  {
    return highName;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public String getType()
  {
    return type;
  }

  public String getExclusive()
  {
    return exclusive;
  }

  public int getBitWidth()
  {
    return bitWidth;
  }

  public boolean isInverting()
  {
    return inverting;
  }

  public int getOffset()
  {
    return offset;
  }

  public boolean isPosition(PortPosition position)
  {
    return this.position == position;
  }

  public void setOffset(int offset)
  {
    this.offset = offset;
  }
}

