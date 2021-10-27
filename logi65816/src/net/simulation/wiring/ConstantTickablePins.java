package net.simulation.wiring;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.*;

public class ConstantTickablePins
    extends Tickable<Snapshot, ConstantTickablePins, Constant>
    implements Pins<Snapshot, ConstantTickablePins, Constant>

{
  protected final Omniport value;

  public ConstantTickablePins(Tickables tickables, Trace trace)
  {
    super(tickables);
    value = new Omniport(this, "Value", 1);
    value.connect(trace);
  }

  public ConstantTickablePins(Tickables tickables, int width, Bus bus)
  {
    super(tickables);
    value = new Omniport(this, "Value", width);
    value.connect(bus);
  }

  public void setValue(long constantValue)
  {
    value.writeAllPinsBool(constantValue);
  }
}

