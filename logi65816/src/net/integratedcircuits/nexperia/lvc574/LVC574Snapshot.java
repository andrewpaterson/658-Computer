package net.integratedcircuits.nexperia.lvc574;

import net.common.Snapshot;

public class LVC574Snapshot
    implements Snapshot
{
  public long latchValue;
  public boolean previousClock;

  public LVC574Snapshot(long latchValue, boolean previousClock)
  {
    this.latchValue = latchValue;
    this.previousClock = previousClock;
  }
}

