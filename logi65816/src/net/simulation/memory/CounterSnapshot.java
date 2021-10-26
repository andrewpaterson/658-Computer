package net.simulation.memory;

import net.common.Snapshot;

public class CounterSnapshot
    implements Snapshot
{
  protected boolean previousClock;
  protected long counter;
  protected long resetValue;

  public CounterSnapshot(boolean previousClock, long counter, long resetValue)
  {
    this.previousClock = previousClock;
    this.counter = counter;
    this.resetValue = resetValue;
  }
}

