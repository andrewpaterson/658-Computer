package net.integratedcircuits.common.counter;

import net.common.Snapshot;

public class UpCounterCircuitSnapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  public boolean reset;
  public boolean clock;

  public UpCounterCircuitSnapshot(long counterValue,
                                  long oldCounterValue,
                                  boolean reset,
                                  boolean clock)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.reset = reset;
    this.clock = clock;
  }
}

