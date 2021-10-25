package net.nexperia.lvc573;

import net.common.IntegratedCircuit;

public class LVC573
    extends IntegratedCircuit
{
  private final LVC573Pins pins;

  public LVC573(LVC573Pins pins)
  {
    this.pins = pins;
    this.pins.setLatch(this);
  }

  @Override
  public void tick()
  {
  }
}

