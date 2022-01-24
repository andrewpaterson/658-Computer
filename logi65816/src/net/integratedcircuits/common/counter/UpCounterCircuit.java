package net.integratedcircuits.common.counter;

import net.common.IntegratedCircuit;
import net.util.StringUtil;

import static net.util.IntUtil.toNybble;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class UpCounterCircuit<SNAPSHOT extends UpCounterCircuitSnapshot, PINS extends UpCounterCircuitPins>
    extends IntegratedCircuit<SNAPSHOT, PINS>
{
  protected int limit;
  protected long counterValue;
  protected long oldCounterValue;
  protected boolean reset;
  protected boolean clock;

  public UpCounterCircuit(String name, UpCounterCircuitPins pins)
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

  protected void parallelLoad(boolean carryInCount, long counterValue, boolean valid)
  {
    if (valid)
    {
      oldCounterValue = this.counterValue;
      this.counterValue = counterValue;

      setOutput(carryInCount);
    }
    setOutput(carryInCount);
  }

  protected int getLimit()
  {
    return limit;
  }

  protected void setOutput(boolean carryIn)
  {
    getPins().setCarry((oldCounterValue + 1 == getLimit()) && carryIn);
    getPins().setOutput(counterValue);
  }

  public String getCounterValueString()
  {
    return StringUtil.getNybbleStringHex(toNybble((int) counterValue));
  }

  @Override
  public void restoreFromSnapshot(UpCounterCircuitSnapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    clock = snapshot.clock;
    reset = snapshot.reset;
    oldCounterValue = snapshot.oldCounterValue;
  }

  protected boolean updateClock(boolean clock)
  {
    boolean clockRising = clock && !this.clock;
    this.clock = clock;
    return clockRising;
  }

  protected void count(boolean carryInCount, boolean countEnabled, boolean clockRising)
  {
    if (countEnabled)
    {
      if (carryInCount && clockRising)
      {
        oldCounterValue = counterValue;
        counterValue++;
        if (counterValue == getLimit())
        {
          counterValue = 0;
        }
      }
    }
    setOutput(carryInCount);
  }
}

