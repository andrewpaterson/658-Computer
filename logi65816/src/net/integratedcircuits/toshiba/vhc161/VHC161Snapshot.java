package net.integratedcircuits.toshiba.vhc161;

import net.integratedcircuits.common.counter.UpCounterCircuitSnapshot;

public class VHC161Snapshot
    extends UpCounterCircuitSnapshot
{
  public VHC161Snapshot(long counterValue,
                        long oldCounterValue,
                        boolean reset,
                        boolean clock,
                        boolean risingEdge)
  {
    super(counterValue, oldCounterValue, reset, clock, risingEdge);
  }
}

