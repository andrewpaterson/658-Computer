package net.integratedcircuits.nexperia.lv165;

import net.common.Snapshot;

public class LV165Snapshot
    implements Snapshot
{
  public int q;
  public boolean qOut;
  public boolean previousCP;
  public boolean previousCEB;

  public LV165Snapshot(int q,
                       boolean qOut,
                       boolean previousCP,
                       boolean previousCEB)
  {
    this.q = q;
    this.qOut = qOut;
    this.previousCP = previousCP;
    this.previousCEB = previousCEB;
  }
}

