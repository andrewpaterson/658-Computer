package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.Trace;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.trace.TraceNet.NotConnected;
import static net.logicim.domain.common.trace.TraceNet.Unsettled;

public interface OutputPropagation
{
  float getHighVoltageOut();

  float getLowVoltageOut();

  int getHighToLowPropagationDelay();

  int getLowToHighPropagationDelay();

  Timeline getTimeline();

  default float getOutputVoltage(boolean value)
  {
    return value ? getHighVoltageOut() : 0.0f;
  }

  default TraceValue getValueOnOutputTrace(Trace trace)
  {
    float traceVoltage = trace.getVoltage();
    if (traceVoltage == Unsettled)
    {
      return TraceValue.Unsettled;
    }
    else if (traceVoltage == NotConnected)
    {
      return TraceValue.NotConnected;
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

  default TraceValue getOutputValue(boolean value)
  {
    return value ? TraceValue.High : TraceValue.Low;
  }

  default void createPropagationEvent(boolean value, Trace trace)
  {
    TraceValue outValue = getOutputValue(value);
    TraceValue traceValue = getValueOnOutputTrace(trace);

    if ((traceValue == TraceValue.Low || traceValue == TraceValue.Unsettled || traceValue == TraceValue.NotConnected) && (outValue == TraceValue.High))
    {
      getTimeline().createPropagationEvent(trace, getHighVoltageOut(), getLowToHighPropagationDelay());
    }
    else if ((traceValue == TraceValue.High || traceValue == TraceValue.Unsettled || traceValue == TraceValue.NotConnected) && (outValue == TraceValue.Low))
    {
      getTimeline().createPropagationEvent(trace, getLowVoltageOut(), getHighToLowPropagationDelay());
    }
  }
}

