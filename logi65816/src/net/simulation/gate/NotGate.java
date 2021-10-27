package net.simulation.gate;

import net.common.PinValue;
import net.common.Snapshot;

public class NotGate
    extends LogicGate<Snapshot, NotGateTickablePins>
{

  public NotGate(String name, NotGateTickablePins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    PinValue inValue = getPins().getInValue();
    if (inValue.isError() || inValue.isNotConnected())
    {
      getPins().setOutError();
    }
    else if (inValue.isUnknown())
    {
      getPins().setOutUnsettled();
    }
    else
    {
      boolean calculatedValue = !inValue.isHigh();
      getPins().setOutValue(calculatedValue);
    }
  }

  @Override
  public String getType()
  {
    return "NOT Gate";
  }
}

