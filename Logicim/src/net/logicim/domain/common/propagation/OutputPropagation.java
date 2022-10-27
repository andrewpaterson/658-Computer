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

  int getHighToLowPropagationDelay();

  int getLowToHighPropagationDelay();

  Timeline getTimeline();

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

  default void createPropagationEvent(TraceValue outValue, TraceNet trace)
  {
    TraceValue traceValue = getValueOnOutputTrace(trace);

    if ((traceValue == TraceValue.Low || traceValue == TraceValue.Unsettled || traceValue == TraceValue.Undriven) && (outValue == TraceValue.High))
    {
      getTimeline().createPropagationEvent(trace, getHighVoltageOut(), getLowToHighPropagationDelay());
    }
    else if ((traceValue == TraceValue.High || traceValue == TraceValue.Unsettled || traceValue == TraceValue.Undriven) && (outValue == TraceValue.Low))
    {
      getTimeline().createPropagationEvent(trace, getLowVoltageOut(), getHighToLowPropagationDelay());
    }
    else if ((traceValue == TraceValue.High || traceValue == TraceValue.Low || traceValue == TraceValue.Unsettled) && (outValue == TraceValue.Undriven))
    {
      getTimeline().createPropagationEvent(trace, getLowVoltageOut(), getHighToLowPropagationDelay());
    }
  }
}

