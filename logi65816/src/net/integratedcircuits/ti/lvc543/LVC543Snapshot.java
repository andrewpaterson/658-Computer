package net.integratedcircuits.ti.lvc543;

import net.common.Snapshot;

public class LVC543Snapshot
    implements Snapshot
{
  public long latchValueA;
  public long latchValueB;

  public LVC543Snapshot(long latchValueA, long latchValue2)
  {
    this.latchValueA = latchValueA;
    this.latchValueB = latchValue2;
  }
}

