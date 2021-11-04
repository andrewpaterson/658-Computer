package net.integratedcircuits.ti.f521;

import net.common.Snapshot;

public class F521Snapshot
    implements Snapshot
{
  public long latchValue;

  public F521Snapshot(long latchValue)
  {
    this.latchValue = latchValue;
  }
}

