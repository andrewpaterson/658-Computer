package net.simulation.wiring;

import net.common.IntegratedCircuit;
import net.common.Snapshot;

public class ClockOscillator
    extends IntegratedCircuit<Snapshot, ClockOscillatorTickablePins>
{
  private boolean value;

  public ClockOscillator(ClockOscillatorTickablePins pins)
  {
    super(pins);
    this.value = false;
  }

  @Override
  public void startTick()
  {
    value = !value;
  }

  @Override
  public void tick()
  {
    getPins().setValue(value);
  }
}

