package net.integratedcircuits.nexperia.lvc574;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC574
    extends IntegratedCircuit<LVC574Snapshot, LVC574Pins>
{
  public static final String TYPE = "8-bit Latch";

  protected long latchValue;
  protected boolean previousClock;

  public LVC574(String name, LVC574Pins pins)
  {
    super(name, pins);
    latchValue = 0;
  }

  @Override
  public void tick()
  {
    PinValue clockValue = getPins().getClock();
    PinValue outputEnabledB = getPins().getOEB();

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHighImpedance = false;
    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      outputError = true;
    }
    else if (outputEnabledB.isUnknown())
    {
      outputUnset = true;
    }
    else if (outputEnabledB.isHigh())
    {
      outputHighImpedance = true;
    }

    boolean clock = clockValue.isHigh();

    boolean clockRisingEdge = clock && !previousClock;
    previousClock = clock;

    if (clockRisingEdge)
    {
      BusValue input = getPins().getInput();
      if (input.isUnknown() || input.isNotConnected())
      {
        outputUnset = true;
      }
      else if (input.isError())
      {
        outputError = true;
      }
      else
      {
        latchValue = input.getValue();
      }
    }

    if (outputError)
    {
      getPins().setOutputError();
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled();
    }
    else if (outputHighImpedance)
    {
      getPins().setOutputHighImpedance();
    }
    else
    {
      getPins().setOutput(latchValue);
    }
  }

  @Override
  public LVC574Snapshot createSnapshot()
  {
    return new LVC574Snapshot(latchValue, previousClock);
  }

  @Override
  public void restoreFromSnapshot(LVC574Snapshot snapshot)
  {
    latchValue = snapshot.latchValue;
    previousClock = snapshot.previousClock;
  }

  public String getValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) latchValue));
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

