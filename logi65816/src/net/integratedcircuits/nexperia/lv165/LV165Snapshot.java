package net.integratedcircuits.nexperia.lv165;

import net.common.Snapshot;

public class LV165Snapshot
    implements Snapshot
{
  public int q;
  public boolean previousCP;
  public boolean previousCEB;

  public LV165Snapshot(int q, boolean previousCP, boolean previousCEB)
  {
    this.q = q;
    this.previousCP = previousCP;
    this.previousCEB = previousCEB;
  }
}

