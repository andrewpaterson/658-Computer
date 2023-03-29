package net.logicim.common.type;

import net.logicim.common.SimulatorException;

public class Int1D
    extends Tuple1
{
  public int i;

  public Int1D(int i)
  {
    this.i = i;
  }

  @Override
  public boolean lessThan(Tuple1 o)
  {
    if (o instanceof Float1D)
    {
      return i < ((Float1D) o).f;
    }
    else if (o instanceof Int1D)
    {
      return i < ((Int1D) o).i;
    }
    else
    {
      throw new SimulatorException("Cannot call lessThan on unknown tuple.");
    }
  }
}

