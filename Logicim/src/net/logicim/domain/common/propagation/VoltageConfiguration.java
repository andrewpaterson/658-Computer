package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.SlewEvent;
import net.logicim.domain.common.trace.TraceValue;

public class VoltageConfiguration
{
  protected String family;

  protected float lowVoltageIn;
  protected float highVoltageIn;

  protected float highVoltageOut;
  protected float lowVoltageOut;
  protected int highToLowHoldTime;
  protected int highToLowSlewTime;
  protected int lowToHighHoldTime;
  protected int lowToHighSlewTime;

  protected float voltsPerTimeLowToHigh;
  protected float voltsPerTimeHighToLow;

  public VoltageConfiguration(String family,
                              float lowVoltageIn,
                              float highVoltageIn,
                              float highVoltageOut,
                              float lowVoltageOut,
                              int highToLowPropagation,
                              int lowToHighPropagation)
  {
    this.family = family;

    this.lowVoltageIn = lowVoltageIn;
    this.highVoltageIn = highVoltageIn;

    this.highVoltageOut = highVoltageOut;
    this.lowVoltageOut = lowVoltageOut;
    this.highToLowHoldTime = highToLowPropagation / 2;
    this.highToLowSlewTime = highToLowPropagation;
    this.lowToHighHoldTime = lowToHighPropagation / 2;
    this.lowToHighSlewTime = lowToHighPropagation;

    this.voltsPerTimeLowToHigh = (highVoltageOut - lowVoltageOut) / lowToHighSlewTime;
    this.voltsPerTimeHighToLow = (highVoltageOut - lowVoltageOut) / lowToHighSlewTime;
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
    else if (Float.isNaN(voltage))
    {
      return TraceValue.Undriven;
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

  public float getLowVoltageOut()
  {
    return lowVoltageOut;
  }

  public float getHighVoltageOut()
  {
    return highVoltageOut;
  }

  public SlewEvent createOutputEvent(Timeline timeline, Port port, float outVoltage)
  {
    float startVoltage = calculateStartVoltage(port.getVoltage(timeline.getTime()));
    float voltageDiff = outVoltage - startVoltage;

    long holdTime;
    if (voltageDiff > 0)
    {
      holdTime = lowToHighHoldTime;
    }
    else if (voltageDiff < 0)
    {
      holdTime = highToLowHoldTime;
    }
    else
    {
      holdTime = (lowToHighHoldTime + highToLowHoldTime) / 2;
    }

    return timeline.createPortSlewEvent(port, holdTime, outVoltage);
  }

  public float calculateStartVoltage(float portVoltage)
  {
    if (!Float.isNaN(portVoltage))
    {
      return portVoltage;
    }
    else
    {
      return getMidVoltageOut();
    }
  }

  public float getMidVoltageOut()
  {
    return (getHighVoltageOut() + getLowVoltageOut()) / 2;
  }

  public void createHighImpedanceEvents(Timeline timeline, Port port)
  {
    throw new SimulatorException("Not yet implemented.");
  }

  public float getVoltsPerTimeLowToHigh()
  {
    return voltsPerTimeLowToHigh;
  }

  public float getVoltsPerTimeHighToLow()
  {
    return voltsPerTimeHighToLow;
  }

  public float getVoltage(boolean value)
  {
    if (value)
    {
      return getHighVoltageOut();
    }
    else
    {
      return getLowVoltageOut();
    }
  }
}

