package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Omniport;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.port.event.OmniportSlewEvent;
import net.logicim.domain.common.port.event.UniportSlewEvent;
import net.logicim.domain.common.trace.TraceValue;

public class BistateOutputVoltage
    extends VoltageConfiguration
    implements OutputVoltageConfiguration
{
  protected float highVoltageOut;
  protected float lowVoltageOut;
  protected int highToLowHoldTime;
  protected int highToLowSlewTime;
  protected int lowToHighHoldTime;
  protected int lowToHighSlewTime;

  protected float voltsPerTimeLowToHigh;
  protected float voltsPerTimeHighToLow;

  public BistateOutputVoltage(String family,
                              float lowVoltageOut,
                              float highVoltageOut,
                              int highToLowPropagation,
                              int lowToHighPropagation)
  {
    super(family);
    this.highVoltageOut = highVoltageOut;
    this.lowVoltageOut = lowVoltageOut;
    this.highToLowHoldTime = highToLowPropagation / 2;
    this.highToLowSlewTime = highToLowPropagation;
    this.lowToHighHoldTime = lowToHighPropagation / 2;
    this.lowToHighSlewTime = lowToHighPropagation;

    this.voltsPerTimeLowToHigh = (highVoltageOut - lowVoltageOut) / lowToHighSlewTime;
    this.voltsPerTimeHighToLow = (highVoltageOut - lowVoltageOut) / lowToHighSlewTime;
  }

  public float getLowVoltageOut()
  {
    return lowVoltageOut;
  }

  public float getHighVoltageOut()
  {
    return highVoltageOut;
  }

  @Override
  public boolean isOutput()
  {
    return true;
  }

  public void createOutputEvents(Timeline timeline, TraceValue outValue, Uniport uniport)
  {
    float startVoltage = getStartVoltage(outValue, uniport);
    float intendedVoltage = getIntendedVoltage(outValue);
    float voltageDiff = intendedVoltage - startVoltage;

    float voltsPerTime;
    long holdTime;
    long slewTime;
    if (voltageDiff > 0)
    {
      voltsPerTime = voltsPerTimeLowToHigh;
      holdTime = lowToHighHoldTime;
      slewTime = (long) (voltageDiff / voltsPerTime);
    }
    else
    {
      voltsPerTime = voltsPerTimeHighToLow;
      holdTime = highToLowHoldTime;
      slewTime = -(long) (voltageDiff / voltsPerTime);
    }

    UniportSlewEvent portSlewEvent = timeline.createPortSlewEvent(uniport, holdTime, slewTime, startVoltage, intendedVoltage);
    timeline.createPortDriveEvent(uniport, portSlewEvent);
  }

  @Override
  public void createOutputEvents(Timeline timeline, TraceValue[] outValues, Omniport omniport)
  {
    long[] holdTimes = new long[outValues.length];
    long[] slewTimes = new long[outValues.length];
    float[] startVoltages = new float[outValues.length];
    float[] intendedVoltages = new float[outValues.length];

    for (int i = 0; i < outValues.length; i++)
    {
      TraceValue outValue = outValues[i];
      float startVoltage = getStartVoltage(outValue, omniport, i);
      float intendedVoltage = getIntendedVoltage(outValue);
      float voltageDiff = intendedVoltage - startVoltage;

      float voltsPerTime;
      long holdTime;
      long slewTime;
      if (voltageDiff > 0)
      {
        voltsPerTime = voltsPerTimeLowToHigh;
        holdTime = lowToHighHoldTime;
        slewTime = (long) (voltageDiff / voltsPerTime);
      }
      else
      {
        voltsPerTime = voltsPerTimeHighToLow;
        holdTime = highToLowHoldTime;
        slewTime = -(long) (voltageDiff / voltsPerTime);
      }

      holdTimes[i] = holdTime;
      slewTimes[i] = slewTime;
      startVoltages[i] = startVoltage;
      intendedVoltages[i] = intendedVoltage;
    }
    OmniportSlewEvent portSlewEvent = timeline.createPortSlewEvent(omniport, holdTimes, slewTimes, startVoltages, intendedVoltages);
    for (int i = 0; i < outValues.length; i++)
    {
      timeline.createPortDriveEvent(omniport, portSlewEvent, i);
    }
  }

  private float getIntendedVoltage(TraceValue value)
  {
    if (value.isLow())
    {
      return getLowVoltageOut();
    }
    else if (value.isHigh())
    {
      return getHighVoltageOut();
    }
    else
    {
      throw new SimulatorException("No intended voltage for trace value [" + value.toEnumString() + "].");
    }
  }

  protected float getStartVoltage(TraceValue outValue, Uniport uniport)
  {
    boolean driven = uniport.isDriven();
    float startVoltage;
    if (driven)
    {
      startVoltage = uniport.getDrivenVoltage();
    }
    else
    {
      if (outValue.isLow())
      {
        startVoltage = getLowVoltageOut();
      }
      else if (outValue.isHigh())
      {
        startVoltage = getHighVoltageOut();
      }
    }
    return startVoltage;
  }

  private float getStartVoltage(TraceValue outValue, Omniport omniport, int busIndex)
  {
    boolean driven = omniport.isDriven();
    float startVoltage;
    if (driven)
    {
      startVoltage = omniport.getDrivenVoltage(busIndex);
    }
    else
    {
      if (outValue.isLow())
      {
        startVoltage = getLowVoltageOut();
      }
      else
      {
        startVoltage = getHighVoltageOut();
      }
    }
    return startVoltage;
  }
}

