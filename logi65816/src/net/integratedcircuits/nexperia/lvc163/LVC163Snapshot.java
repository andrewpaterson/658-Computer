package net.integratedcircuits.nexperia.lvc163;

import net.integratedcircuits.common.counter.CounterCircuitSnapshot;

public class LVC163Snapshot
    extends CounterCircuitSnapshot
{
  public LVC163Snapshot(long counterValue,
                        long oldCounterValue,
                        boolean reset,
                        boolean clock,
                        boolean fallingEdge,
                        boolean risingEdge)
  {
    super(counterValue, oldCounterValue, reset, clock, fallingEdge, risingEdge);
  }
}

