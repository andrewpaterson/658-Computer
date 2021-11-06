package net.integratedcircuits.nexperia.lvc162373;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC162373
    extends IntegratedCircuit<LVC162373Snapshot, LVC162373Pins>
{
  public static final String TYPE = "16-bit Latch";

  protected long[] latchValue;

  public LVC162373(String name, LVC162373Pins pins)
  {
    super(name, pins);
    latchValue = new long[2];
  }

  @Override
  public void tick()
  {
    tickLatch(0);
    tickLatch(1);
  }

  private void tickLatch(int index)
  {
    PinValue latchEnabled = getPins().getLE(index);
    PinValue outputEnabledB = getPins().getOEB(index);

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
      BusValue input = getPins().getInput(index);
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
        latchValue[index] = input.getValue();
      }
    }

    if (outputError)
    {
      getPins().setOutputError(index);
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled(index);
    }
    else if (outputHighImpedance)
    {
      getPins().setOutputHighImpedance(index);
    }
    else
    {
      getPins().setOutput(index, latchValue[index]);
    }
  }

  @Override
  public LVC162373Snapshot createSnapshot()
  {
    return new LVC162373Snapshot(latchValue[0], latchValue[1]);
  }

  @Override
  public void restoreFromSnapshot(LVC162373Snapshot snapshot)
  {
    latchValue[0] = snapshot.latchValue1;
    latchValue[1] = snapshot.latchValue2;
  }

  public String getValueString(int index)
  {
    return StringUtil.getByteStringHex(toByte((int) latchValue[index]));
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

