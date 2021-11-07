package net.integratedcircuits.common.counter;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public abstract class CounterCircuit<SNAPSHOT extends CounterCircuitSnapshot, PINS extends CounterCircuitPins>
    extends IntegratedCircuit<SNAPSHOT, PINS>
{
  protected int limit;
  protected long counterValue;
  protected long oldCounterValue;
  protected boolean reset;
  protected boolean clock;
  protected boolean fallingEdge;
  protected boolean risingEdge;

  public CounterCircuit(String name, CounterCircuitPins pins)
  {
    super(name, (PINS) pins);
    counterValue = 0;
    oldCounterValue = 0;
    reset = false;
  }

  protected void reset()
  {
    counterValue = 0;
    oldCounterValue = 0;
    getPins().setOutput(0);
    getPins().setCarry(false);
  }

  protected void parallelLoad(boolean carryInCount)
  {
    BusValue input = getPins().getInput();
    if (input.isValid())
    {
      oldCounterValue = counterValue;
      counterValue = input.getValue();

      setOutput(carryInCount);
    }
    setOutput(carryInCount);
  }

  protected int getLimit()
  {
    return limit;
  }

  protected void setOutput(boolean cet)
  {
    getPins().setCarry((oldCounterValue + 1 == getLimit()) && cet);
    getPins().setOutput(counterValue);
  }

  public String getCounterValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) counterValue));
  }

  @Override
  public void restoreFromSnapshot(CounterCircuitSnapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    clock = snapshot.clock;
    reset = snapshot.reset;
    fallingEdge = snapshot.fallingEdge;
    risingEdge = snapshot.risingEdge;
    oldCounterValue = snapshot.oldCounterValue;
  }

  protected void updateClock(PinValue clockValue)
  {
    boolean currentClock = clockValue.isHigh();
    this.fallingEdge = !currentClock && this.clock;
    this.risingEdge = currentClock && !this.clock;
    this.clock = currentClock;
  }

  protected void count(boolean carryInCount, boolean countEnabled)
  {
    if (countEnabled)
    {
      if (carryInCount && risingEdge)
      {
        oldCounterValue = counterValue;
        counterValue++;
        if (counterValue == getLimit())
        {
          counterValue = 0;
        }
      }
      setOutput(carryInCount);
    }
  }
}

