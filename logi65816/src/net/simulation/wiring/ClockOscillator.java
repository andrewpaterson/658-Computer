package net.simulation.wiring;

import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;
import net.simulation.gate.Tickable;

public class ClockOscillator
    extends Tickable
{
  private final Uniport out;
  private boolean value;

  public ClockOscillator(Tickables tickables, String name, Trace trace)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(trace);
    this.value = false;
  }

  @Override
  public void propagate()
  {
    out.writeBool(value);
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
  public void startPropagation()
  {
    value = !value;
  }

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }
}

