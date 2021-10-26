package net.maxim.ds1813;

import net.common.Snapshot;

public class DS1813Snapshot
    implements Snapshot
{
  public int count;

  public DS1813Snapshot(int count)
  {
    this.count = count;
  }
}

