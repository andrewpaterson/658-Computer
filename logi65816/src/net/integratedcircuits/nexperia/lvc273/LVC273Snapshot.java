package net.integratedcircuits.nexperia.lvc273;

import net.common.Snapshot;

public class LVC273Snapshot
    implements Snapshot
{
  public long latchValue;
  public boolean clock;

  public LVC273Snapshot(long latchValue, boolean clock)
  {
    this.latchValue = latchValue;
    this.clock = clock;
  }
}

