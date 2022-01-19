package net.integratedcircuits.common.counter;

import net.common.Snapshot;

public class UpCounterCircuitSnapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  public boolean reset;
  public boolean clock;
  public boolean clockRising;

  public UpCounterCircuitSnapshot(long counterValue,
                                  long oldCounterValue,
                                  boolean reset,
                                  boolean clock,
                                  boolean clockRising)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.reset = reset;
    this.clock = clock;
    this.clockRising = clockRising;
  }
}

