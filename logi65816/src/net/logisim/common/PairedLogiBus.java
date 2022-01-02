package net.logisim.common;

public class PairedLogiBus
{
  public LogiBus left;
  public LogiBus right;

  public PairedLogiBus(LogiBus left, LogiBus right)
  {
    this.left = left;
    this.right = right;
  }
}

