package net.integratedcircuits.nexperia.lvc161;

import net.integratedcircuits.common.counter.UpCounterCircuitSnapshot;

public class LVC161Snapshot
    extends UpCounterCircuitSnapshot
{
  public LVC161Snapshot(long counterValue,
                        long oldCounterValue,
                        boolean reset,
                        boolean clock)
  {
    super(counterValue, oldCounterValue, reset, clock);
  }
}

