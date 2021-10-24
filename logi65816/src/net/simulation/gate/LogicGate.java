package net.simulation.gate;

import net.simulation.common.*;

import java.util.List;

import static net.simulation.common.TraceValue.High;
import static net.simulation.common.TraceValue.Low;

public abstract class LogicGate
    extends Tickable
{
  public LogicGate(Tickables tickables, String name)
  {
    super(tickables, name);
  }

  protected void propagateLogic(List<Uniport> in, Uniport out, TraceValue defaultValue)
  {
    TraceValue inValues = Port.readStates(in);
    if (inValues.isError() || inValues.isNotConnected())
    {
      out.error();
    }
    else if (inValues.isUnsettled())
    {
      out.unset();
    }
    else
    {
      TraceValue oppositeValue = defaultValue == High ? Low : High;
      TraceValue outputValue;
      outputValue = defaultValue;
      for (Uniport input : in)
      {
        TraceValue inValue = input.read();
        if (inValue == oppositeValue)
        {
          outputValue = oppositeValue;
          break;
        }
      }

      out.writeBool(outputValue == High);
    }
  }

  @Override
  public void startPropagation()
  {
  }

  @Override
  public void donePropagation()
  {
  }
}

