package net.integratedcircuits.nexperia.lvc157;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC157
    extends IntegratedCircuit<LVC157Snapshot, LVC157Pins>
{
  public static final String TYPE = "4-bit Multiplexer";

  public LVC157(String name, LVC157Pins pins)
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
    PinValue enabledB = getPins().getEB();

    if (enabledB.isError() || enabledB.isNotConnected())
    {
      getPins().setYError();
    }
    else
    {
      boolean enabled = enabledB.isLow();
      if (enabled)
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
        getPins().setYValue(0);
      }
    }
  }

  public LVC157Snapshot createSnapshot()
  {
    return new LVC157Snapshot();
  }

  public void restoreFromSnapshot(LVC157Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

