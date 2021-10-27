package net.simulation.wiring;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.*;

public class ClockOscillatorTickablePins
    extends Tickable<Snapshot, ClockOscillatorTickablePins, ClockOscillator>
    implements Pins<Snapshot, ClockOscillatorTickablePins, ClockOscillator>
{
  private final Uniport out;

  public ClockOscillatorTickablePins(Tickables tickables, Trace trace)
  {
    super(tickables);
    this.out = new Uniport(this, "Out");
    this.out.connect(trace);
  }

  public void setValue(boolean value)
  {
    out.writeBool(value);
  }
}

