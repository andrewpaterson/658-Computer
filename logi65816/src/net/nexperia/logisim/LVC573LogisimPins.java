package net.nexperia.logisim;

import net.common.IntegratedCircuit;
import net.logisim.common.LogisimPins;
import net.nexperia.lvc573.LVC573;
import net.nexperia.lvc573.LVC573Pins;

public class LVC573LogisimPins
    extends LogisimPins
    implements LVC573Pins
{
  protected LVC573 latch;

  public LVC573LogisimPins()
  {
    new LVC573(this);
  }

  @Override
  public IntegratedCircuit getIntegratedCircuit()
  {
    return latch;
  }

  @Override
  public void setLatch(LVC573 latch)
  {
    this.latch = latch;
  }
}

