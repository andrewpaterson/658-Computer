package net.nexperia.lvc573;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC573
    implements IntegratedCircuit
{
  private final LVC573Pins pins;

  protected long latchValue;

  public LVC573(LVC573Pins pins)
  {
    this.pins = pins;
    this.pins.setLatch(this);
    latchValue = 0;
  }

  @Override
  public void tick()
  {
    PinValue latchEnabled = pins.getLE();
    PinValue outputEnabledB = pins.getOEB();

    boolean outputError = false;
    boolean outputUnset = false;
    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      outputError = true;
    }
    else if (outputEnabledB.isUnknown())
    {
      outputUnset = true;
    }

    if (latchEnabled.isHigh())
    {
      BusValue input = pins.getInput();
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
      pins.setOutputError();
    }
    else if (outputUnset)
    {
      pins.setOutputUnsettled();
    }
    else
    {
      pins.setOutput(latchValue);
    }
  }

  public LVC573Snapshot createSnapshot()
  {
    return new LVC573Snapshot(latchValue);
  }

  public void restoreFromSnapshot(LVC573Snapshot snapshot)
  {
    latchValue = snapshot.latchValue;
  }

  public String getValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) latchValue));
  }
}

