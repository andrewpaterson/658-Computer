package net.common.type;

import net.common.SimulatorException;

public class Float1D
    extends Tuple1
{
  public float f;

  public Float1D(float f)
  {
    this.f = f;
  }

  @Override
  public boolean lessThan(Tuple1 o)
  {
    if (o instanceof Float1D)
    {
      return f < ((Float1D) o).f;
    }
    else if (o instanceof Int1D)
    {
      return f < ((Int1D) o).i;
    }
    else
    {
      throw new SimulatorException("Cannot call lessThan on unknown tuple.");
    }
  }
}

