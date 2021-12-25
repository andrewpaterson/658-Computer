package net.simulation.memory;

import net.common.PinValue;
import net.common.Pins;
import net.simulation.common.*;

public class CounterTickablePins
    extends TickablePins<CounterSnapshot, CounterTickablePins, Counter>
    implements Pins<CounterSnapshot, CounterTickablePins, Counter>
{
  protected final Omniport value;
  protected final Uniport clock;

  private final int width;

  public CounterTickablePins(Tickables tickables, int width, Bus dataBus, Trace clockTrace)
  {
    super(tickables);
    this.value = new Omniport(this, "Value", width);
    this.clock = new Uniport(this, "Clk");

    value.connect(dataBus);
    clock.connect(clockTrace);

    this.width = width;
  }

  public int getWidth()
  {
    return width;
  }

  public PinValue getClock()
  {
    return getPinValue(clock);
  }

  public void setValueError()
  {
    value.error();
  }

  public void setValueUnsettled()
  {
    value.unset();
  }

  public void setValue(long counter)
  {
    value.writeAllPinsBool(counter);
  }
}

