package net.integratedcircuits.ti.hct40103;

import net.common.Snapshot;

public class HCT40103Snapshot
    implements Snapshot
{
  protected long counterValue;
  protected long syncLoadValue;
  protected boolean clock;

  public HCT40103Snapshot(long counterValue, long syncLoadValue, boolean clock)
  {
    this.counterValue = counterValue;
    this.syncLoadValue = syncLoadValue;
    this.clock = clock;
  }
}

