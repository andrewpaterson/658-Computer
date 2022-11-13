package net.logicim.domain.common.propagation;

import net.logicim.domain.common.trace.TraceValue;

public class InputVoltage
    extends VoltageConfiguration
{
  private float lowVoltageIn;
  private float highVoltageIn;

  public InputVoltage(String family, float lowVoltageIn, float highVoltageIn)
  {
    super(family);
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
    if (voltage <= lowVoltageIn)
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

  public float getLowVoltageIn()
  {
    return lowVoltageIn;
  }

  public float getHighVoltageIn()
  {
    return highVoltageIn;
  }
}

