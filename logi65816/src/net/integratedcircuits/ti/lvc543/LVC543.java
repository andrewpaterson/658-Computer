package net.integratedcircuits.ti.lvc543;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC543
    extends IntegratedCircuit<LVC543Snapshot, LVC543Pins>
{
  public static final String TYPE = "Octal Bi-latch";

  public static final int AB = 0;
  public static final int BA = 1;

  protected long[] latchValue;

  public LVC543(String name, LVC543Pins pins)
  {
    super(name, pins);
    latchValue = new long[2];
  }

  @Override
  public void tick()
  {
    tickLatch(AB);
    tickLatch(BA);
  }

  private void tickLatch(int index)
  {
    PinValue latchEnabledB = getPins().getLEB(index);
    PinValue outputEnabledB = getPins().getOEB(index);
    PinValue chipEnableB = getPins().getCEB(index);

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

    if (latchEnabledB.isHigh())
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

