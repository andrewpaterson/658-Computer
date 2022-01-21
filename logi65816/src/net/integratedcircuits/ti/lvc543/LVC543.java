package net.integratedcircuits.ti.lvc543;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC543
    extends IntegratedCircuit<LVC543Snapshot, LVC543Pins>
{
  public static final String TYPE = "8-bit Bi-latch";

  public static final int A = 0;
  public static final int B = 1;

  protected long[] latchValue;

  public LVC543(String name, LVC543Pins pins)
  {
    super(name, pins);
    latchValue = new long[2];
  }

  @Override
  public void tick()
  {
    tickLatch(A, B);
    tickLatch(B, A);
  }

  private void tickLatch(int inputIndex, int outputIndex)
  {
    PinValue latchEnabledB = getPins().getLEB(inputIndex);
    PinValue outputEnabledB = getPins().getOEB(inputIndex);
    PinValue chipEnableB = getPins().getCEB(inputIndex);

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHighImpedance = false;

    if ((outputEnabledB.isError() || chipEnableB.isError()) ||
        (outputEnabledB.isNotConnected() || chipEnableB.isNotConnected()))
    {
      outputError = true;
    }
    else if (outputEnabledB.isHigh() || chipEnableB.isHigh())
    {
      outputHighImpedance = true;
    }
    else if (!outputEnabledB.isLow() && !chipEnableB.isLow())
    {
      outputUnset = true;
    }

    if (latchEnabledB.isLow() && chipEnableB.isLow())
    {
      BusValue input = getPins().getInput(inputIndex);
      if (input.isUnknown() || input.isNotConnected())
      {
        outputUnset = true;
      }
      else if (input.isValid())
      {
        latchValue[inputIndex] = input.getValue();
      }
    }

    if (outputError)
    {
      getPins().setOutputError(outputIndex);
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled(outputIndex);
    }
    else if (outputHighImpedance)
    {
      getPins().setOutputHighImpedance(outputIndex);
    }
    else
    {
      getPins().setOutput(outputIndex, latchValue[inputIndex]);
    }
  }

  @Override
  public LVC543Snapshot createSnapshot()
  {
    return new LVC543Snapshot(latchValue[0], latchValue[1]);
  }

  @Override
  public void restoreFromSnapshot(LVC543Snapshot snapshot)
  {
    latchValue[0] = snapshot.latchValueA;
    latchValue[1] = snapshot.latchValueB;
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

