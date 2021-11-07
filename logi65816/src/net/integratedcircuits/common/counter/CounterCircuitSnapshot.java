package net.integratedcircuits.common.counter;

import net.common.Snapshot;

public class CounterCircuitSnapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  public boolean reset;
  public boolean clock;
  public boolean fallingEdge;
  public boolean risingEdge;

  public CounterCircuitSnapshot(long counterValue, long oldCounterValue, boolean reset, boolean clock, boolean fallingEdge, boolean risingEdge)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.reset = reset;
    this.clock = clock;
    this.fallingEdge = fallingEdge;
    this.risingEdge = risingEdge;
  }
}

