package net.integratedcircuits.ti.ls148;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LS148
    extends IntegratedCircuit<LS148Snapshot, LS148Pins>
{
  public static final String TYPE = "8-to-3 Encoder";

  public LS148(String name, LS148Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    PinValue enabledInput = getPins().getEI();

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHigh = false;
    if (enabledInput.isError() || enabledInput.isNotConnected())
    {
      outputError = true;
    }
    else if (enabledInput.isUnknown())
    {
      outputUnset = true;
    }
    else if (enabledInput.isHigh())
    {
      outputHigh = true;
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
      getPins().setA(0x07);
      getPins().setGS(true);
      getPins().setEO(true);
    }
    else
    {
      BusValue input = getPins().getInput();
      if (input.isUnknown() || input.isNotConnected())
      {
        getPins().setOutputUnsettled();
      }
      else if (input.isError())
      {
        getPins().setOutputError();
      }
      else
      {
        long inputValue = input.getValue();
        inputValue = ~inputValue & 0xff;
        int outputValue = 0;

        while (inputValue != 0)
        {
          outputValue++;
          inputValue >>= 1;
        }

        if (outputValue > 0)
        {
          getPins().setA(8 - outputValue);
          getPins().setGS(false);
          getPins().setEO(true);
        }
        else
        {
          getPins().setA(0x07);
          getPins().setGS(true);
          getPins().setEO(false);
        }
      }
    }
  }

  @Override
  public LS148Snapshot createSnapshot()
  {
    return new LS148Snapshot();
  }

  @Override
  public void restoreFromSnapshot(LS148Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

