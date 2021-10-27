package net.simulation.memory;

import net.common.PinValue;
import net.common.Pins;
import net.simulation.common.*;

public class CounterTickablePins
    extends Tickable<CounterSnapshot, CounterTickablePins, Counter>
    implements Pins<CounterSnapshot, CounterTickablePins, Counter>
{
  protected final Omniport value;
  protected final Uniport phi2;

  private final int width;

  public CounterTickablePins(Tickables tickables, String name, int width, Bus dataBus, Trace clockTrace)
  {
    super(tickables);
    this.value = new Omniport(this, "Value", width);
    this.phi2 = new Uniport(this, "Clk");

    value.connect(dataBus);
    phi2.connect(clockTrace);

    this.width = width;
  }

  public int getWidth()
  {
    return width;
  }

  public PinValue getPhi2()
  {
    return getPinValue(phi2);
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

