package net.integratedcircuits.nexperia.lvc4245;

import net.common.PinValue;
import net.common.Snapshot;

public class LVC4245Snapshot
    implements Snapshot
{
  public final PinValue direction;

  public LVC4245Snapshot(PinValue direction)
  {
    this.direction = direction;
  }
}

