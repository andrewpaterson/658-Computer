package net.integratedcircuits.nexperia.lvc157;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC1G157
    extends IntegratedCircuit<LVC1G157Snapshot, LVC1G157Pins>
{
  public static final String TYPE = "1-of-2 Multiplexer";

  public LVC1G157(String name, LVC1G157Pins pins)
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
        PinValue readValue = getPins().getInputValue((int) port);
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
          getPins().setYValue(value == 1);
        }
      }
      else
      {
        getPins().setYValue(false);
      }
    }
  }

  public LVC1G157Snapshot createSnapshot()
  {
    return new LVC1G157Snapshot();
  }

  public void restoreFromSnapshot(LVC1G157Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

