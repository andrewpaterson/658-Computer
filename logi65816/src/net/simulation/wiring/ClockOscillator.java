package net.simulation.wiring;

import net.common.IntegratedCircuit;
import net.common.Snapshot;

public class ClockOscillator
    extends IntegratedCircuit<Snapshot, ClockOscillatorTickablePins>
{
  private boolean value;

  public ClockOscillator(String name, ClockOscillatorTickablePins pins)
  {
    super(name, pins);
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

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }
}

