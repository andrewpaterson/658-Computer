package net.integratedcircuits.nexperia.hc590;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class HC590
    extends IntegratedCircuit<HC590Snapshot, HC590Pins>
{
  public static final String TYPE = "8-bit Counter";

  protected int limit;
  protected long counterValue;
  protected long oldCounterValue;
  protected long registerValue;
  protected boolean reset;
  protected boolean clock;
  protected boolean clockRisingEdge;
  protected boolean registerClock;
  protected boolean registerClockRisingEdge;

  public HC590(String name, HC590Pins pins)
  {
    super(name, pins);
    limit = 0x100;
  }

  @Override
  public void tick()
  {
    PinValue masterResetBValue = getPins().getMasterResetB();
    PinValue clockValue = getPins().getClock();
    PinValue countEnabledValue = getPins().getCountEnabledB();
    PinValue counterToRegisterClockValue = getPins().getCounterToRegisterClock();
    PinValue outputEnabledB = getPins().getOutputEnabledB();

    boolean previousReset = reset;
    reset = masterResetBValue.isLow();
    updateClock(clockValue.isHigh(), counterToRegisterClockValue.isHigh());

    if (previousReset)
    {
      counterValue = 0;
      oldCounterValue = 0;
    }
    else if (countEnabledValue.isHigh())
    {
      if (clockRisingEdge)
      {
        oldCounterValue = counterValue;
        counterValue++;
        if (counterValue == limit)
        {
          counterValue = 0;
        }
      }
    }

    if (registerClockRisingEdge)
    {
      registerValue = oldCounterValue;
    }

    getPins().setCarry(!(oldCounterValue + 1 == limit));
    if (outputEnabledB.isLow())
    {
      getPins().setOutput(registerValue & 0xff);
    }
    else if (outputEnabledB.isHigh())
    {
      getPins().setOutputHighImpedance();
    }
  }

  protected void updateClock(boolean currentClock, boolean currentRegisterClock)
  {
    this.clockRisingEdge = currentClock && !this.clock;
    this.clock = currentClock;

    this.registerClockRisingEdge = currentRegisterClock && !this.registerClock;
    this.registerClock = currentRegisterClock;
  }

  @Override
  public HC590Snapshot createSnapshot()
  {
    return new HC590Snapshot(counterValue,
                             oldCounterValue,
                             registerValue,
                             reset,
                             clock,
                             clockRisingEdge,
                             registerClock,
                             registerClockRisingEdge);
  }

  @Override
  public void restoreFromSnapshot(HC590Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    oldCounterValue = snapshot.oldCounterValue;
    registerValue = snapshot.registerValue;
    reset = snapshot.reset;
    clock = snapshot.clock;
    clockRisingEdge = snapshot.clockRisingEdge;
    registerClock = snapshot.registerClock;
    registerClockRisingEdge = snapshot.registerClockRisingEdge;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getCounterValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) counterValue));
  }

  public String getRegisterValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) registerValue));
  }
}

