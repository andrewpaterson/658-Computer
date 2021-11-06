package net.integratedcircuits.nexperia.lvc373;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC373
    extends IntegratedCircuit<LVC373Snapshot, LVC373Pins>
{
  public static final String TYPE = "Octal Latch";

  protected long latchValue;

  public LVC373(String name, LVC373Pins pins)
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
  public LVC373Snapshot createSnapshot()
  {
    return new LVC373Snapshot(latchValue);
  }

  @Override
  public void restoreFromSnapshot(LVC373Snapshot snapshot)
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

