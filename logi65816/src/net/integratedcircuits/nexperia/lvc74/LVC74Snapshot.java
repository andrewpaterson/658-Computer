package net.integratedcircuits.nexperia.lvc74;

import net.common.Snapshot;

public class LVC74Snapshot
    implements Snapshot
{
  public boolean risingValue1;
  public boolean risingValue2;
  public boolean fallingValue1;
  public boolean fallingValue2;
  public boolean clock1;
  public boolean clock2;
  public boolean clockRising1;
  public boolean clockRising2;

  public LVC74Snapshot(boolean risingValue1,
                       boolean risingValue2,
                       boolean fallingValue1,
                       boolean fallingValue2,
                       boolean clock1,
                       boolean clock2,
                       boolean clockRising1,
                       boolean clockRising2)
  {
    this.risingValue1 = risingValue1;
    this.risingValue2 = risingValue2;
    this.fallingValue1 = fallingValue1;
    this.fallingValue2 = fallingValue2;
    this.clock1 = clock1;
    this.clock2 = clock2;
    this.clockRising1 = clockRising1;
    this.clockRising2 = clockRising2;
  }
}

