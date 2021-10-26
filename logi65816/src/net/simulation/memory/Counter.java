package net.simulation.memory;

import net.common.IntegratedCircuit;
import net.simulation.common.*;

public class Counter
    extends Tickable
    implements IntegratedCircuit
{
  protected final Omniport value;
  protected final Uniport phi2;

  protected boolean previousClock;
  protected long counter;
  protected long resetValue;

  protected CounterSnapshot snapshot;

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

    snapshot = null;
  }

  @Override
  public void startPropagation()
  {
    snapshot = new CounterSnapshot(previousClock, counter, resetValue);
  }

  public void undoPropagation()
  {
    if (snapshot != null)
    {
      restoreFromSnapShot(snapshot);
    }
  }

  private void restoreFromSnapShot(CounterSnapshot snapshot)
  {
    counter = snapshot.counter;
    previousClock = snapshot.previousClock;
    resetValue = snapshot.resetValue;
  }

  @Override
  public void donePropagation()
  {
    snapshot = null;
  }

  @Override
  public String getType()
  {
    return "Counter";
  }

  @Override
  protected IntegratedCircuit getIntegratedCircuit()
  {
    return this;
  }

  public long getCounter()
  {
    return counter;
  }

  @Override
  public void tick()
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
}

