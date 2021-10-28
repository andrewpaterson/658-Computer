package net.maxim.ds1813;

import net.common.IntegratedCircuit;

public class DS1813
    extends IntegratedCircuit<DS1813Snapshot, DS1813Pins>
{
  public static final String TYPE = "EconoReset";

  public DS1813(String name, DS1813Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
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
}

