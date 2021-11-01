package net.logisim.common;

public class LogiPin
{
  public int index;
  public int propagationDelay;

  public LogiPin(int index, int propagationDelay)
  {
    this.index = index;
    this.propagationDelay = propagationDelay;
  }
}

