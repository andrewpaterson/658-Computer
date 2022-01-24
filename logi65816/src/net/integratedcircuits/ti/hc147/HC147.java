package net.integratedcircuits.ti.hc147;

import net.common.BusValue;
import net.common.IntegratedCircuit;

public class HC147
    extends IntegratedCircuit<HC147Snapshot, HC147Pins>
{
  public static final String TYPE = "10-to-4 Encoder";

  public HC147(String name, HC147Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
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
      inputValue = ~inputValue & 0x1ff;
      int outputValue = 0;

      while (inputValue != 0)
      {
        outputValue++;
        inputValue >>= 1;
      }

      getPins().setOutput((~outputValue) & 0xf);
    }
  }

  @Override
  public HC147Snapshot createSnapshot()
  {
    return new HC147Snapshot();
  }

  @Override
  public void restoreFromSnapshot(HC147Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

