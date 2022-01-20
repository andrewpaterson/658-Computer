package net.integratedcircuits.nexperia.hct193;

import net.common.Snapshot;

public class HCT193Snapshot
    implements Snapshot
{
  protected long counterValue;
  protected boolean upClock;
  protected boolean downClock;

  public HCT193Snapshot(long counterValue,
                        boolean upClock,
                        boolean downClock)
  {
    this.counterValue = counterValue;
    this.upClock = upClock;
    this.downClock = downClock;
  }
}

