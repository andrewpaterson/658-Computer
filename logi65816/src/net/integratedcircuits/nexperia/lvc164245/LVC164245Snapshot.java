package net.integratedcircuits.nexperia.lvc164245;

import net.common.PinValue;
import net.common.Snapshot;

public class LVC164245Snapshot
    implements Snapshot
{
  public final PinValue direction1;
  public final PinValue direction2;

  public LVC164245Snapshot(PinValue direction1, PinValue direction2)
  {
    this.direction1 = direction1;
    this.direction2 = direction2;
  }
}

