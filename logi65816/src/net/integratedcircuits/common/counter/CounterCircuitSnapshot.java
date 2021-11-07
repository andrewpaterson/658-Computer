package net.integratedcircuits.common.counter;

import net.common.Snapshot;

public class CounterCircuitSnapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  public boolean reset;
  public boolean clock;
  public boolean clockRisingEdge;

  public CounterCircuitSnapshot(long counterValue,
                                long oldCounterValue,
                                boolean reset,
                                boolean clock,
                                boolean clockRisingEdge)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.reset = reset;
    this.clock = clock;
    this.clockRisingEdge = clockRisingEdge;
  }
}

