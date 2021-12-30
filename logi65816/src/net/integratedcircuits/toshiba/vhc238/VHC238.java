package net.integratedcircuits.toshiba.vhc238;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class VHC238
    extends IntegratedCircuit<VHC238Snapshot, VHC238Pins>
{
  public static final String TYPE = "3-to-8 Decoder";

  public VHC238(String name, VHC238Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    PinValue enabled1B = getPins().getE1B();
    PinValue enabled2B = getPins().getE2B();
    PinValue enabled3 = getPins().getE3();

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHigh = false;
    if (enabled1B.isError() || enabled1B.isNotConnected() ||
        enabled2B.isError() || enabled2B.isNotConnected() ||
        enabled3.isError() || enabled3.isNotConnected())
    {
      outputError = true;
    }
    else if (enabled1B.isUnknown() ||
             enabled2B.isUnknown() ||
             enabled3.isUnknown())
    {
      outputUnset = true;
    }
    else if (enabled1B.isHigh() || enabled2B.isHigh() || enabled3.isLow())
    {
      outputHigh = true;
    }

    BusValue input = getPins().getA();

    if (input.isUnknown() || input.isNotConnected())
    {
      outputUnset = true;
    }
    else if (input.isError())
    {
      outputError = true;
    }

    if (outputError)
    {
      getPins().setOutputError();
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled();
    }
    else if (outputHigh)
    {
      getPins().setOutput(0);
    }
    else
    {
      long outputValue = input.getValue();
      outputValue = 1L << outputValue;
      getPins().setOutput(outputValue);
    }
  }

  @Override
  public VHC238Snapshot createSnapshot()
  {
    return new VHC238Snapshot();
  }

  @Override
  public void restoreFromSnapshot(VHC238Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

