package net.integratedcircuits.nexperia.lvc257;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC257
    extends IntegratedCircuit<LVC257Snapshot, LVC257Pins>
{
  public static final String TYPE = "4-bit Multiplexer";

  public LVC257(String name, LVC257Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    PinValue selectorValue = getPins().getSelector();
    if (selectorValue.isError() || selectorValue.isNotConnected())
    {
      getPins().setYError();
      return;
    }
    else if (selectorValue.isUnknown())
    {
      getPins().setYUnsettled();
      return;
    }

    transmit(selectorValue.getValue());
  }

  private void transmit(long port)
  {
    PinValue outputEnabledB = getPins().getOEB();

    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      getPins().setYError();
    }
    else
    {
      boolean outputEnabled = outputEnabledB.isLow();
      if (outputEnabled)
      {
        BusValue readValue = getPins().getInputValue((int) port);
        if (readValue.isError() || readValue.isNotConnected())
        {
          getPins().setYError();
        }
        else if (readValue.isUnknown())
        {
          getPins().setYUnsettled();
        }
        else
        {
          long value = readValue.getValue();
          getPins().setYValue(value);
        }
      }
      else
      {
        getPins().setYHighImpedance();
      }
    }
  }

  public LVC257Snapshot createSnapshot()
  {
    return new LVC257Snapshot();
  }

  public void restoreFromSnapshot(LVC257Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

