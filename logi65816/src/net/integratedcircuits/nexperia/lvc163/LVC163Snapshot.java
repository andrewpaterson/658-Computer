package net.integratedcircuits.nexperia.lvc163;

import net.integratedcircuits.common.counter.CounterCircuitSnapshot;

public class LVC163Snapshot
    extends CounterCircuitSnapshot
{
  public LVC163Snapshot(long counterValue,
                        long oldCounterValue,
                        boolean reset,
                        boolean clock,
                        boolean risingEdge)
  {
    super(counterValue, oldCounterValue, reset, clock, risingEdge);
  }
}

