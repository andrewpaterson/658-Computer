package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.common.util.StringUtil;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

public class BistateOutputPropagation
    extends Propagation
    implements OutputPropagation
{
  protected float highVoltageOut;
  protected float lowVoltageOut;
  protected int highToLowSustain;
  protected int highToLowDecay;
  protected int lowToHigSustain;
  protected int lowToHigDecay;

  public BistateOutputPropagation(String family,
                                  float lowVoltageOut,
                                  float highVoltageOut,
                                  int highToLowPropagation,
                                  int lowToHighPropagation)
  {
    super(family);
    this.highVoltageOut = highVoltageOut;
    this.lowVoltageOut = lowVoltageOut;
    this.highToLowSustain = highToLowPropagation / 2;
    this.highToLowDecay = highToLowPropagation;
    this.lowToHigSustain = lowToHighPropagation / 2;
    this.lowToHigDecay = lowToHighPropagation;
  }

  @Override
  public float getLowVoltageOut()
  {
    return lowVoltageOut;
  }

  @Override
  public float getHighVoltageOut()
  {
    return highVoltageOut;
  }

  public int getHighToLowSustain()
  {
    return highToLowSustain;
  }

  public int getLowToHigSustain()
  {
    return lowToHigSustain;
  }

  @Override
  public int getHighToLowDecay()
  {
    return highToLowDecay;
  }

  @Override
  public int getLowToHigDecay()
  {
    return lowToHigDecay;
  }

  @Override
  public boolean isOutput()
  {
    return true;
  }

  public void createPropagationEvent(Timeline timeline, TraceValue outValue, TraceNet trace)
  {
    TraceValue traceValue = getValueOnOutputTrace(trace);

    if (outValue == TraceValue.High)
    {
      if (traceValue != TraceValue.High)
      {
        timeline.createPropagationEvent(trace, getUnsettledVoltageOut(), getLowToHigSustain());
        timeline.createPropagationEvent(trace, getHighVoltageOut(), getLowToHigDecay());
      }
    }
    else if (outValue == TraceValue.Low)
    {
      if (traceValue != TraceValue.Low)
      {
        timeline.createPropagationEvent(trace, getUnsettledVoltageOut(), getHighToLowSustain());
        timeline.createPropagationEvent(trace, getLowVoltageOut(), getHighToLowDecay());
      }
    }
    else if (outValue == TraceValue.Unsettled)
    {
      if (traceValue != TraceValue.Unsettled)
      {
        timeline.createPropagationEvent(trace, getUnsettledVoltageOut(), getHighToLowSustain());
      }
    }
    else
    {
      throw new SimulatorException("Bi-state output cannot change state to [" + StringUtil.toEnumString(outValue) + "].");
    }
  }
}

