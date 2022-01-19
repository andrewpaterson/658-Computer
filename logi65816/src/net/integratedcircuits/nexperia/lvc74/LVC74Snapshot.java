package net.integratedcircuits.nexperia.lvc74;

import net.common.Snapshot;

public class LVC74Snapshot
    implements Snapshot
{
  public boolean value1;
  public boolean value2;
  public boolean clock1;
  public boolean clock2;
  public boolean clockRising1;
  public boolean clockRising2;

  public LVC74Snapshot(boolean value1,
                       boolean value2,
                       boolean clock1,
                       boolean clock2,
                       boolean clockRising1,
                       boolean clockRising2)
  {
    this.value1 = value1;
    this.value2 = value2;
    this.clock1 = clock1;
    this.clock2 = clock2;
    this.clockRising1 = clockRising1;
    this.clockRising2 = clockRising2;
  }
}

