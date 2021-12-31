package net.simulation.integratedcircuits.maxim;

import net.common.PinValue;
import net.integratedcircuits.maxim.ds1813.DS1813;
import net.integratedcircuits.maxim.ds1813.DS1813Pins;
import net.integratedcircuits.maxim.ds1813.DS1813Snapshot;
import net.simulation.common.TickablePins;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

public class DS1813TickablePins
    extends TickablePins<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  private final Uniport out;
  private final Uniport reset;

  private long resetTickTarget;

  public DS1813TickablePins(Tickables tickables, Trace out, Trace reset)
  {
    super(tickables);
    this.out = new Uniport(this, "Out");
    this.out.connect(out);

    this.reset = new Uniport(this, "Reset");
    this.reset.connect(reset);
    this.resetTickTarget = 0x7FFFFFFFFFFFFFFFL;
  }

  @Override
  public void setOut()
  {
    boolean value = getTickCount() > resetTickTarget;
    out.writeBool(value);
  }

  @Override
  public PinValue getReset()
  {
    return getPinValue(reset);
  }

  @Override
  public void startCounter()
  {
    resetTickTarget = getTickCount() + getIntegratedCircuit().getResetTickCount();
  }
}

