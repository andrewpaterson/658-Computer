package net.integratedcircuits.nexperia.hc590;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class HC590
    extends IntegratedCircuit<HC590Snapshot, HC590Pins>
{
  public static final String TYPE = "8-bit Up Counter";

  protected int limit;
  protected long counterValue;
  protected long oldCounterValue;
  protected long registerValue;
  protected boolean reset;
  protected boolean clock;
  protected boolean registerClock;

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
    PinValue countEnabledB = getPins().getCountEnabledB();
    PinValue counterToRegisterClockValue = getPins().getCounterToRegisterClock();
    PinValue outputEnabledB = getPins().getOutputEnabledB();

    boolean previousReset = reset;
    reset = masterResetBValue.isLow();

    boolean currentClock = clockValue.isHigh();
    boolean currentRegisterClock = counterToRegisterClockValue.isHigh();

    boolean clockRising = currentClock && !this.clock;
    this.clock = currentClock;

    boolean registerClockRising = currentRegisterClock && !this.registerClock;
    this.registerClock = currentRegisterClock;

    boolean carry = false;
    if (previousReset)
    {
      counterValue = 0;
      oldCounterValue = 0;
    }
    else if (countEnabledB.isLow())
    {
      if (clockRising)
      {
        counterValue++;
        if (counterValue == limit)
        {
          counterValue = 0;
          carry = true;
        }
      }
    }

    if (registerClockRising)
    {
      registerValue = oldCounterValue;
    }

    getPins().setCarry(!carry);

    oldCounterValue = counterValue;

    if (outputEnabledB.isLow())
    {
      getPins().setOutput(registerValue & 0xff);
    }
    else if (outputEnabledB.isHigh())
    {
      getPins().setOutputHighImpedance();
    }
  }

  @Override
  public HC590Snapshot createSnapshot()
  {
    return new HC590Snapshot(counterValue,
                             oldCounterValue,
                             registerValue,
                             reset,
                             clock,
                             registerClock);
  }

  @Override
  public void restoreFromSnapshot(HC590Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    oldCounterValue = snapshot.oldCounterValue;
    registerValue = snapshot.registerValue;
    reset = snapshot.reset;
    clock = snapshot.clock;
    registerClock = snapshot.registerClock;
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

