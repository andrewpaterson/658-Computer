package net.simulation.memory;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class Counter
    extends IntegratedCircuit<CounterSnapshot, CounterTickablePins>
{
  protected boolean clock;
  protected long counter;
  protected long resetValue;

  public Counter(String name, CounterTickablePins pins)
  {
    super(name, pins);
    clock = true;
    counter = 0;
    resetValue = 1L << pins.getWidth();
  }

  @Override
  public CounterSnapshot createSnapshot()
  {
    return new CounterSnapshot(clock, counter);
  }

  @Override
  public void restoreFromSnapshot(CounterSnapshot snapshot)
  {
    counter = snapshot.counter;
    clock = snapshot.clock;
  }

  public long getCounter()
  {
    return counter;
  }

  @Override
  public void tick()
  {
    PinValue clockValue = getPins().getClock();
    if (clockValue.isError())
    {
      getPins().setValueError();
    }
    else if (clockValue.isUnknown())
    {
      getPins().setValueUnsettled();
    }
    else if (clockValue.isNotConnected())
    {
      getPins().setValue(counter);
    }
    else
    {
      boolean clock = clockValue.isHigh();

      boolean clockRisingEdge = clock && !this.clock;
      this.clock = clock;

      if (clockRisingEdge)
      {
        counter++;
        if (counter == resetValue)
        {
          counter = 0;
        }
      }
      getPins().setValue(counter);
    }
  }

  @Override
  public String getType()
  {
    return "Up Counter";
  }
}

