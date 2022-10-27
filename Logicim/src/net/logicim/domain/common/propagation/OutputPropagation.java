package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.trace.TraceNet.Undriven;
import static net.logicim.domain.common.trace.TraceNet.Unsettled;

public interface OutputPropagation
{
  float getHighVoltageOut();

  float getLowVoltageOut();

  //How long after an input change the transition from unsettled to low finishes.
  int getHighToLowDecay();

  int getLowToHigDecay();

  //How long after an input change the transition from previous value (high) to unsettled finishes.
  int getHighToLowSustain();

  int getLowToHigSustain();

  default TraceValue getValueOnOutputTrace(TraceNet trace)
  {
    if (trace == null)
    {
      return TraceValue.Undriven;
    }

    float traceVoltage = trace.getVoltage();
    if (traceVoltage == Unsettled)
    {
      return TraceValue.Unsettled;
    }
    else if (traceVoltage == Undriven)
    {
      return TraceValue.Undriven;
    }
    else
    {
      if (getHighVoltageOut() > traceVoltage)
      {
        return TraceValue.Low;
      }
      else
      {
        return TraceValue.High;
      }
    }
  }

  default float getUnsettledVoltageOut()
  {
    return Unsettled;
  }

  void createPropagationEvent(Timeline timeline, TraceValue outValue, TraceNet trace);
}

