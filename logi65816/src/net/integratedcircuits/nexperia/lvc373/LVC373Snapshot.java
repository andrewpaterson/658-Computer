package net.integratedcircuits.nexperia.lvc373;

import net.common.Snapshot;

public class LVC373Snapshot
    implements Snapshot
{
  public long latchValue;

  public LVC373Snapshot(long latchValue)
  {
    this.latchValue = latchValue;
  }
}

