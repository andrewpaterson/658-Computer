package net.common.util;

public class Counter
{
  public long count;

  public Counter()
  {
  }

  public long tick()
  {
    return count++;
  }
}

