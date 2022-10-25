package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

public class InputPropagation
    extends Propagation
{
  private float lowVoltageIn;
  private float highVoltageIn;

  public InputPropagation(String family, float lowVoltageIn, float highVoltageIn)
  {
    super(null, family);
    this.lowVoltageIn = lowVoltageIn;
    this.highVoltageIn = highVoltageIn;
  }

  @Override
  public boolean isInput()
  {
    return true;
  }

  public TraceValue getValue(float voltage)
  {
    if (voltage == TraceNet.Unsettled)
    {
      return TraceValue.Unsettled;
    }
    else if (voltage == TraceNet.NotConnected)
    {
      return TraceValue.NotConnected;
    }
    else if (voltage <= lowVoltageIn)
    {
      return TraceValue.Low;
    }
    else if (voltage >= highVoltageIn)
    {
      return TraceValue.High;
    }
    else
    {
      return TraceValue.Unsettled;
    }
  }
}

