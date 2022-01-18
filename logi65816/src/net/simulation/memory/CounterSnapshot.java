package net.simulation.memory;

import net.common.Snapshot;

public class CounterSnapshot
    implements Snapshot
{
  protected boolean clock;
  protected long counter;

  public CounterSnapshot(boolean clock, long counter)
  {
    this.clock = clock;
    this.counter = counter;
  }
}

