package net.nexperia.lvc16373;

import net.common.Snapshot;

public class LVC16373Snapshot
    implements Snapshot
{
  public long latchValue1;
  public long latchValue2;

  public LVC16373Snapshot(long latchValue1, long latchValue2)
  {
    this.latchValue1 = latchValue1;
    this.latchValue2 = latchValue2;
  }
}

