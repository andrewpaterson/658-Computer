package net.maxim.ds1813;

import net.common.IntegratedCircuit;

public class DS1813
    extends IntegratedCircuit<DS1813Snapshot, DS1813Pins>
{
  private int count;

  public DS1813(String name, DS1813Pins pins)
  {
    super(name, pins);
    this.count = 4;
  }

  @Override
  public void tick()
  {
    if (count > 0)
    {
      count--;
      getPins().setOut(false);

    }
    else
    {
      getPins().setOut(true);
    }
  }

  @Override
  public DS1813Snapshot createSnapshot()
  {
    return new DS1813Snapshot(count);
  }

  @Override
  public void restoreFromSnapshot(DS1813Snapshot snapshot)
  {
    count = snapshot.count;
  }

  @Override
  public String getType()
  {
    return "DS1813";
  }
}

