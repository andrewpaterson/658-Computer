package net.integratedcircuits.nexperia.lvc161;

import net.integratedcircuits.common.counter.CounterCircuitSnapshot;

public class LVC161Snapshot
    extends CounterCircuitSnapshot
{
  public LVC161Snapshot(long counterValue,
                        long oldCounterValue,
                        boolean reset,
                        boolean clock,
                        boolean risingEdge)
  {
    super(counterValue, oldCounterValue, reset, clock, risingEdge);
  }
}

