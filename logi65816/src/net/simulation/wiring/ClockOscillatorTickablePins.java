package net.simulation.wiring;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.TickablePins;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

public class ClockOscillatorTickablePins
    extends TickablePins<Snapshot, ClockOscillatorTickablePins, ClockOscillator>
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

