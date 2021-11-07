package net.integratedcircuits.nexperia.lvc139;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC139
    extends IntegratedCircuit<LVC139Snapshot, LVC139Pins>
{
  public static final String TYPE = "2-to-4 Decoder";

  public LVC139(String name, LVC139Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    tickPort(0);
    tickPort(1);
  }

  private void tickPort(int port)
  {
    PinValue enabledB = getPins().getEB(port);

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHigh = false;
    if (enabledB.isError() || enabledB.isNotConnected())
    {
      outputError = true;
    }
    else if (enabledB.isUnknown())
    {
      outputUnset = true;
    }
    else if (enabledB.isHigh())
    {
      outputHigh = true;
    }

    BusValue input = getPins().getA(port);

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
      getPins().setOutputError(port);
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled(port);
    }
    else if (outputHigh)
    {
      getPins().setOutput(port, 0xf);
    }
    else
    {
      long outputValue = input.getValue();
      outputValue = (~(1L << outputValue)) & 0xf;
      getPins().setOutput(port, outputValue);
    }
  }

  @Override
  public LVC139Snapshot createSnapshot()
  {
    return new LVC139Snapshot();
  }

  @Override
  public void restoreFromSnapshot(LVC139Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

