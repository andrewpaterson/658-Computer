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

  public void createOutputEvents(Timeline timeline, boolean outValue, Port port)
  {
    float startVoltage = calculateStartVoltage(port, timeline.getTime());
    float intendedVoltage = getVoltage(outValue);
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

    SlewEvent portSlewEvent = timeline.createPortSlewEvent(port, holdTime, slewTime, startVoltage, intendedVoltage);
    timeline.createPortDriveEvent(port, portSlewEvent);
  }

  protected float calculateStartVoltage(Port port, long time)
  {
    float voltage = port.getVoltage(time);
    if (!Float.isNaN(voltage))
    {
      return voltage;
    }
    else
    {
      return (getHighVoltageOut() + getLowVoltageOut()) / 2;
    }
  }

  @Override
  public void createHighImpedanceEvents(Timeline timeline, Port port)
  {
    throw new SimulatorException("Not yet implemented.");
  }

  private float getVoltage(boolean value)
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

  protected float calculateVoltageFromBool(boolean outValue)
  {
    return getVoltage(!outValue);
  }
}

