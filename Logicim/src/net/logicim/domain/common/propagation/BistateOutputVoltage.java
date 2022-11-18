package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.SlewEvent;

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

  @Override
  public boolean isOutput()
  {
    return true;
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

  @Override
  public float getMidVoltageOut()
  {
    return (getHighVoltageOut() + getLowVoltageOut()) / 2;
  }

  @Override
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
}

