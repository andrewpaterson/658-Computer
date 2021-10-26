package net.simulation.wiring;

import net.common.IntegratedCircuit;
import net.common.Snapshot;

public class Constant
    extends IntegratedCircuit<Snapshot, ConstantTickablePins>
{
  protected long constantValue;

  public Constant(ConstantTickablePins pins, boolean booleanValue)
  {
    super(pins);
    setValue(booleanValue);
  }

  public Constant(ConstantTickablePins pins, long constantValue)
  {
    super(pins);
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

  @Override
  public void tick()
  {
    getPins().setValue(constantValue);
  }
}

