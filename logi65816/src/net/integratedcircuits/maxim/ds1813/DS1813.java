package net.integratedcircuits.maxim.ds1813;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class DS1813
    extends IntegratedCircuit<DS1813Snapshot, DS1813Pins>
{
  public static final String TYPE = "EconoReset";

  public int stretch;

  public DS1813(String name, DS1813Pins pins)
  {
    super(name, pins);
    stretch = 3;
    startCounter();
  }

  private void startCounter()
  {
    getPins().startCounter();
  }

  @Override
  public void tick()
  {
    PinValue reset = getPins().getReset();
    if (!reset.isHigh())
    {
      startCounter();
    }

    getPins().setOut();
  }

  @Override
  public DS1813Snapshot createSnapshot()
  {
    return new DS1813Snapshot();
  }

  @Override
  public void restoreFromSnapshot(DS1813Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public int getResetTickCount()
  {
    return 5 * stretch;
  }

  public int getStretch()
  {
    return stretch;
  }
}

