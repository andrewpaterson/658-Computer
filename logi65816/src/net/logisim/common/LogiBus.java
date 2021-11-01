package net.logisim.common;

public class LogiBus
{
  public int index;
  public int width;
  public int propagationDelay;

  public LogiBus(int index, int width, int propagationDelay)
  {
    this.index = index;
    this.width = width;
    this.propagationDelay = propagationDelay;
  }
}

