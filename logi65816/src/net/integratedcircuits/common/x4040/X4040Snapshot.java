package net.integratedcircuits.common.x4040;

import net.common.Snapshot;

public class X4040Snapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  public boolean reset;
  public boolean clock;
  public boolean clockFallingEdge;

  public X4040Snapshot(long counterValue,
                       long oldCounterValue,
                       boolean reset,
                       boolean clock,
                       boolean clockFallingEdge)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.reset = reset;
    this.clock = clock;
    this.clockFallingEdge = clockFallingEdge;
  }
}

