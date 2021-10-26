package net.maxim.simulation;

import net.maxim.ds1813.DS1813;
import net.maxim.ds1813.DS1813Pins;
import net.maxim.ds1813.DS1813Snapshot;
import net.simulation.common.Tickable;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

public class DS1813TickablePins
    extends Tickable<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  private final Uniport out;

  public DS1813TickablePins(Tickables tickables, String name, Trace out)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(out);
  }

  @Override
  public String getType()
  {
    return "DS1813";
  }

  @Override
  public void setOut(boolean value)
  {
    out.writeBool(value);
  }
}

