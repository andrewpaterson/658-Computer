package net.integratedcircuits.toshiba.vhc393;

import net.common.Snapshot;
import net.integratedcircuits.common.counter.UpCounterCircuitSnapshot;

public class VHC393Snapshot
    implements Snapshot
{
  public long counterValue1;
  public boolean reset1;
  public boolean clock1;
  public long counterValue2;
  public boolean reset2;
  public boolean clock2;

  public VHC393Snapshot(long counterValue1,
                        boolean reset1,
                        boolean clock1,
                        long counterValue2,
                        boolean reset2,
                        boolean clock2)
  {
    this.counterValue1 = counterValue1;
    this.reset1 = reset1;
    this.clock1 = clock1;
    this.counterValue2 = counterValue2;
    this.reset2 = reset2;
    this.clock2 = clock2;
  }
}

