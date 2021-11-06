package net.integratedcircuits.nexperia.lvc162373;

import net.common.Snapshot;

public class LVC162373Snapshot
    implements Snapshot
{
  public long latchValue1;
  public long latchValue2;

  public LVC162373Snapshot(long latchValue1, long latchValue2)
  {
    this.latchValue1 = latchValue1;
    this.latchValue2 = latchValue2;
  }
}

