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
  private boolean drawBar;
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
    this.inverting = false;
    this.drawBar = false;
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

  public PortDescription setInverting()
  {
    this.inverting = true;
    return this;
  }

  public PortDescription setDrawBar()
  {
    this.drawBar = true;
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

  public boolean isDrawBar()
  {
    return drawBar;
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

  public LogiPin createPin(int propagationDelay)
  {
    return new LogiPin(index, propagationDelay);
  }

  public LogiBus createBus(int propagationDelay)
  {
    return new LogiBus(index, bitWidth, propagationDelay);
  }
}

