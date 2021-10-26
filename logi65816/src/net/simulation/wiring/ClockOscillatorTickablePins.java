package net.simulation.wiring;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.*;

public class ClockOscillatorTickablePins
    extends Tickable<Snapshot, ClockOscillatorTickablePins, ClockOscillator>
    implements Pins<Snapshot, ClockOscillatorTickablePins, ClockOscillator>
{
  private final Uniport out;

  public ClockOscillatorTickablePins(Tickables tickables, String name, Trace trace)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(trace);
  }

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }

  public void setValue(boolean value)
  {
    out.writeBool(value);
  }
}

