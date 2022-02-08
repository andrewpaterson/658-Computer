package net.integratedcircuits.nexperia.lvc595;

import net.common.Snapshot;

public class LVC595Snapshot
    implements Snapshot
{
  public int shiftRegister;
  public int storageRegister;
  public boolean prevSHCP;
  public boolean prevSTCP;
  public boolean qs7;

  public LVC595Snapshot(int shiftRegister,
                        int storageRegister,
                        boolean prevSHCP,
                        boolean prevSTCP,
                        boolean qs7)
  {
    this.shiftRegister = shiftRegister;
    this.storageRegister = storageRegister;
    this.prevSHCP = prevSHCP;
    this.prevSTCP = prevSTCP;
    this.qs7 = qs7;
  }
}

