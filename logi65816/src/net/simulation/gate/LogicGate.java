package net.simulation.gate;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.common.Snapshot;

import java.util.List;

import static net.common.PinValue.High;
import static net.common.PinValue.Low;

public abstract class LogicGate<SNAPSHOT extends Snapshot, PINS extends LogicGateTickablePins<SNAPSHOT, PINS, ? extends IntegratedCircuit<SNAPSHOT, PINS>>>
    extends IntegratedCircuit<SNAPSHOT, PINS>
{
  public LogicGate(String name, PINS pins)
  {
    super(name, pins);
  }

  protected void andOrLogic(PinValue defaultValue)
  {
    PinValue inValues = getPins().getSingleInputValue();

    if (inValues.isError() || inValues.isNotConnected())
    {
      getPins().setOutError();
    }
    else if (inValues.isUnknown())
    {
      getPins().setOutUnsettled();
    }
    else
    {
      PinValue oppositeValue = defaultValue == High ? Low : High;
      PinValue outputValue;
      outputValue = defaultValue;
      List<PinValue> pinValues = getPins().getInputValues();
      for (PinValue inValue : pinValues)
      {
        if (inValue == oppositeValue)
        {
          outputValue = oppositeValue;
          break;
        }
      }

      getPins().setOutValue(outputValue == High);
    }
  }
}

