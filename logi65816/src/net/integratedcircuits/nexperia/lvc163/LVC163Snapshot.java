package net.integratedcircuits.nexperia.lvc163;

import net.common.Snapshot;

public class LVC163Snapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;

  public boolean clock;
  public boolean fallingEdge;
  public boolean risingEdge;

  public LVC163Snapshot(long counterValue, long oldCounterValue, boolean clock, boolean fallingEdge, boolean risingEdge)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.clock = clock;
    this.fallingEdge = fallingEdge;
    this.risingEdge = risingEdge;
  }
}

