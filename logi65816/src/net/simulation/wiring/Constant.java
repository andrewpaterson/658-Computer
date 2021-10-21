package net.simulation.wiring;

import net.simulation.common.Bus;
import net.simulation.common.Omniport;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.gate.Tickable;

public class Constant
    extends Tickable
{
  protected final Omniport value;

  protected long constantValue;

  public Constant(Tickables tickables, String name, boolean booleanValue, Trace trace)
  {
    super(tickables, name);
    value = new Omniport(this, "Value", 1);
    value.connect(trace);

    setValue(booleanValue);
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

  public Constant(Tickables tickables, String name, int width, long constantValue, Bus bus)
  {
    super(tickables, name);
    value = new Omniport(this, "Value", width);
    value.connect(bus);

    this.constantValue = constantValue;
  }

  @Override
  public void startPropagation()
  {
  }

  @Override
  public void propagate()
  {
    value.writeAllPinsBool(constantValue);
  }

  @Override
  public void undoPropagation()
  {
  }

  @Override
  public void donePropagation()
  {
  }

  @Override
  public String getType()
  {
    return "Constant";
  }
}

