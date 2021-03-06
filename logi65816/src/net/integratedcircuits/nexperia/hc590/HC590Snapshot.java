package net.integratedcircuits.nexperia.hc590;

import net.common.Snapshot;

public class HC590Snapshot
    implements Snapshot
{
  public long counterValue;
  public long oldCounterValue;
  protected long registerValue;
  public boolean reset;
  public boolean clock;
  public boolean registerClock;

  public HC590Snapshot(long counterValue,
                       long oldCounterValue,
                       long registerValue,
                       boolean reset,
                       boolean clock,
                       boolean registerClock)
  {
    this.counterValue = counterValue;
    this.oldCounterValue = oldCounterValue;
    this.registerValue = registerValue;
    this.reset = reset;
    this.clock = clock;
    this.registerClock = registerClock;
  }
}

