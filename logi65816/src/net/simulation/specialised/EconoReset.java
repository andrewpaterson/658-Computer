package net.simulation.specialised;

import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;
import net.simulation.gate.Tickable;

public class EconoReset
    extends Tickable
{
  private final Uniport out;
  private int count;

  private int oldCount;

  public EconoReset(Tickables tickables, String name, Trace out)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(out);
    this.count = 4;
  }

  @Override
  public void startPropagation()
  {
    oldCount = count;
  }

  @Override
  public void propagate()
  {
    if (count > 0)
    {
      count--;
      out.writeBool(false);
    }
    else
    {
      out.writeBool(true);
    }
  }

  @Override
  public void undoPropagation()
  {
    count = oldCount;
  }

  @Override
  public void donePropagation()
  {
  }

  @Override
  public String getType()
  {
    return null;
  }
}

