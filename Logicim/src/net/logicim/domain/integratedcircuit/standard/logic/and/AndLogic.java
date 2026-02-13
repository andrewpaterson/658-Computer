package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.wire.TraceValue;

import java.util.List;

public class AndLogic
{
  public void inputTransition(Timeline timeline, List<LogicPort> inputs, LogicPort output)
  {
    int lows = 0;
    int highs = 0;
    for (LogicPort input : inputs)
    {
      TraceValue inValue = input.readValue(timeline);
      if (inValue.isLow())
      {
        lows++;
      }
      else if (inValue.isHigh())
      {
        highs++;
      }
    }

    if (lows > 0)
    {
      output.writeBool(timeline, transformOutput(false));
    }
    else if (highs > 0)
    {
      output.writeBool(timeline, transformOutput(true));
    }
  }

  protected boolean transformOutput(boolean value)
  {
    return value;
  }
}

