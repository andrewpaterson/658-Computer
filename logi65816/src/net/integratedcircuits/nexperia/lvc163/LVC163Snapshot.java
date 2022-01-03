package net.integratedcircuits.nexperia.lvc163;

import net.integratedcircuits.common.counter.UpCounterCircuitSnapshot;

public class LVC163Snapshot
    extends UpCounterCircuitSnapshot
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

