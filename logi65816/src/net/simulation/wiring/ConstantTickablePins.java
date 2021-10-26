package net.simulation.wiring;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.*;

public class ConstantTickablePins
    extends Tickable<Snapshot, ConstantTickablePins, Constant>
    implements Pins<Snapshot, ConstantTickablePins, Constant>

{
  protected final Omniport value;

  public ConstantTickablePins(Tickables tickables, String name, Trace trace)
  {
    super(tickables, name);
    value = new Omniport(this, "Value", 1);
    value.connect(trace);
  }

  public ConstantTickablePins(Tickables tickables, String name, int width, Bus bus)
  {
    super(tickables, name);
    value = new Omniport(this, "Value", width);
    value.connect(bus);
  }

  @Override
  public String getType()
  {
    return "Constant";
  }

  public void setValue(long constantValue)
  {
    value.writeAllPinsBool(constantValue);
  }
}

