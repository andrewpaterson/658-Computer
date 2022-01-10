package net.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class F521
    extends IntegratedCircuit<F521Snapshot, F521Pins>
{
  public static final String TYPE = "8-bit Comparator";

  protected long latchValue;

  public F521(String name, F521Pins pins)
  {
    super(name, pins);
    latchValue = 0;
  }

  @Override
  public void tick()
  {
    PinValue outputEnabledB = getPins().getOEB();
    BusValue pValue = getPins().getP();
    BusValue qValue = getPins().getQ();

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHigh = false;
    if (outputEnabledB.isError() || outputEnabledB.isNotConnected() ||
        pValue.isError() || pValue.isNotConnected() ||
        qValue.isError() || qValue.isNotConnected())
    {
      outputError = true;
    }
    else if (outputEnabledB.isUnknown())
    {
      outputUnset = true;
    }
    else if (outputEnabledB.isHigh())
    {
      outputHigh = true;
    }

    if (outputError)
    {
      getPins().setQEqualPError();
    }
    else if (outputUnset)
    {
      getPins().setQEqualPUnsettled();
    }
    else if (outputHigh)
    {
      getPins().setQEqualP(true);
    }
    else
    {
      long p = pValue.getValue();
      long q = qValue.getValue();

      getPins().setQEqualP(p != q);
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

