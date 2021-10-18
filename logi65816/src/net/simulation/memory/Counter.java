package net.simulation.memory;

import net.simulation.common.*;
import net.simulation.gate.Tickable;

public class Counter
    extends Tickable
{
  protected final Omniport value;
  protected final Uniport phi2;

  protected boolean previousClock;
  protected long counter;
  protected long resetValue;

  protected boolean previousPreviousClock;
  protected long previousCounter;

  public Counter(Tickables tickables, String name, int width, Bus dataBus, Trace clockTrace)
  {
    super(tickables, name);
    this.value = new Omniport(this, "Value", width);
    this.phi2 = new Uniport(this, "Clk");

    value.connect(dataBus);
    phi2.connect(clockTrace);

    previousClock = true;
    counter = 0;
    resetValue = 1L << width;
  }

  @Override
  public void startPropagation()
  {
    previousCounter = counter;
    previousPreviousClock = previousClock;
  }

  @Override
  public void propagate()
  {
    TraceValue clockValue = phi2.read();
    if (clockValue.isError())
    {
      value.error();
    }
    else if (clockValue.isUnsettled())
    {
      value.unset();
    }
    else if (clockValue.isNotConnected())
    {
      value.writeAllPinsBool(counter);
    }
    else
    {
      boolean clock = phi2.getBoolAfterRead();

      boolean clockRisingEdge = clock && !previousClock;
      previousClock = clock;

      if (clockRisingEdge)
      {
        counter++;
        if (counter == resetValue)
        {
          counter = 0;
        }
      }
      value.writeAllPinsBool(counter);
    }
  }

  @Override
  public void undoPropagation()
  {
    counter = previousCounter;
    previousClock = previousPreviousClock;
  }

  @Override
  public void donePropagation()
  {
  }

  @Override
  public String getType()
  {
    return "Counter";
  }

  public long getCounter()
  {
    return counter;
  }
}

