package net.simulation.wiring;

import net.common.IntegratedCircuit;
import net.common.Snapshot;

public class Constant
    extends IntegratedCircuit<Snapshot, ConstantTickablePins>
{
  protected long constantValue;

  public Constant(String name, ConstantTickablePins pins, boolean booleanValue)
  {
    super(name, pins);
    setValue(booleanValue);
  }

  public Constant(String name, ConstantTickablePins pins, long constantValue)
  {
    super(name, pins);
    this.constantValue = constantValue;
  }

  public void setValue(boolean booleanValue)
  {
    if (booleanValue)
    {
      constantValue = 1;
    }
    else
    {
      constantValue = 0;
    }
  }

  public void setValue(long value)
  {
    this.constantValue = value;
  }

  @Override
  public void tick()
  {
    getPins().setValue(constantValue);
  }

  @Override
  public String getType()
  {
    return "Constant";
  }
}

