package name.bizna.bus.wiring;

import name.bizna.bus.common.Bus;
import name.bizna.bus.common.Omniport;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.gate.Tickable;

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
  public void propagate()
  {
    value.writeAllPinsBool(constantValue);
  }

  @Override
  public String getType()
  {
    return "Constant";
  }
}

