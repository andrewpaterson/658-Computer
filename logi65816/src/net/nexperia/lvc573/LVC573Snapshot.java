package net.nexperia.lvc573;

import net.common.Snapshot;

public class LVC573Snapshot
    implements Snapshot
{
  public long latchValue;

  public LVC573Snapshot(long latchValue)
  {
    this.latchValue = latchValue;
  }
}

