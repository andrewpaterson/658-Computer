package net.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class F521
    extends IntegratedCircuit<F521Snapshot, F521Pins>
{
  public static final String TYPE = "Octal Latch";

  protected long latchValue;

  public F521(String name, F521Pins pins)
  {
    super(name, pins);
    latchValue = 0;
  }

  @Override
  public void tick()
  {
    PinValue latchEnabled = getPins().getLE();
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

    if (latchEnabled.isHigh())
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
  public F521Snapshot createSnapshot()
  {
    return new F521Snapshot(latchValue);
  }

  @Override
  public void restoreFromSnapshot(F521Snapshot snapshot)
  {
    latchValue = snapshot.latchValue;
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

