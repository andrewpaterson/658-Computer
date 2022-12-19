package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.SlewEvent;
import net.logicim.domain.common.trace.TraceValue;

public class VoltageConfiguration
    implements Comparable<VoltageConfiguration>
{
  protected float vcc;

  protected float lowVoltageIn;
  protected float highVoltageIn;

  protected float highVoltageOut;
  protected float lowVoltageOut;

  protected int highToLowHoldTime;
  protected int highToLowSlewTime;
  protected int lowToHighHoldTime;
  protected int lowToHighSlewTime;

  protected int highToDisableHoldTime;
  protected int highToDisableSlewTime;
  protected int lowToDisableHoldTime;
  protected int lowToDisableSlewTime;

  protected int highToEnableHoldTime;
  protected int highToEnableSlewTime;
  protected int lowToEnableHoldTime;
  protected int lowToEnableSlewTime;

  public VoltageConfiguration(float vcc,
                              float lowVoltageIn,
                              float highVoltageIn,
                              float lowVoltageOut,
                              float highVoltageOut,
                              int highToLowPropagation,
                              int lowToHighPropagation)
  {
    this.vcc = vcc;

    setVoltages(lowVoltageIn, highVoltageIn, lowVoltageOut, highVoltageOut);
    setPropagationTime(highToLowPropagation, lowToHighPropagation);

    this.highToDisableHoldTime = (int) (highToLowHoldTime * 1.2f);
    this.highToDisableSlewTime = (int) (highToLowSlewTime * 1.2f);
    this.lowToDisableHoldTime = (int) (lowToHighHoldTime * 1.2f);
    this.lowToDisableSlewTime = (int) (lowToHighSlewTime * 1.2f);

    this.highToEnableHoldTime = (int) (highToLowHoldTime * 1.2f);
    this.highToEnableSlewTime = (int) (highToLowSlewTime * 1.2f);
    this.lowToEnableHoldTime = (int) (lowToHighHoldTime * 1.2f);
    this.lowToEnableSlewTime = (int) (lowToHighSlewTime * 1.2f);

    validateVoltages();
    validateSlewTimes();
  }

  protected void setVoltages(float lowVoltageIn, float highVoltageIn, float lowVoltageOut, float highVoltageOut)
  {
    this.lowVoltageIn = lowVoltageIn;
    this.highVoltageIn = highVoltageIn;

    this.highVoltageOut = highVoltageOut;
    this.lowVoltageOut = lowVoltageOut;
  }

  public VoltageConfiguration(float vcc,
                              float lowVoltageIn,
                              float highVoltageIn,
                              float lowVoltageOut,
                              float highVoltageOut,
                              int highToLowPropagation,
                              int lowToHighPropagation,
                              int highToDisEnPropagation,
                              int lowToDisEnPropagation)
  {
    this.vcc = vcc;

    setVoltages(lowVoltageIn, highVoltageIn, lowVoltageOut, highVoltageOut);

    setPropagationTime(highToLowPropagation, lowToHighPropagation);
    setDisableTime(highToDisEnPropagation, lowToDisEnPropagation);
    setEnableTime(highToDisEnPropagation, lowToDisEnPropagation);

    validateVoltages();
    validateSlewTimes();
  }

  public VoltageConfiguration(float vcc,
                              float lowVoltageIn,
                              float highVoltageIn,
                              float highVoltageOut,
                              float lowVoltageOut,
                              int highToLowHoldTime,
                              int highToLowSlewTime,
                              int lowToHighHoldTime,
                              int lowToHighSlewTime,
                              int highToDisableHoldTime,
                              int highToDisableSlewTime,
                              int lowToDisableHoldTime,
                              int lowToDisableSlewTime,
                              int highToEnableHoldTime,
                              int highToEnableSlewTime,
                              int lowToEnableHoldTime,
                              int lowToEnableSlewTime)
  {
    this.vcc = vcc;

    setVoltages(lowVoltageIn, highVoltageIn, lowVoltageOut, highVoltageOut);

    this.highToLowHoldTime = highToLowHoldTime;
    this.highToLowSlewTime = highToLowSlewTime;
    this.lowToHighHoldTime = lowToHighHoldTime;
    this.lowToHighSlewTime = lowToHighSlewTime;

    this.highToDisableHoldTime = highToDisableHoldTime;
    this.highToDisableSlewTime = highToDisableSlewTime;
    this.lowToDisableHoldTime = lowToDisableHoldTime;
    this.lowToDisableSlewTime = lowToDisableSlewTime;

    this.highToEnableHoldTime = highToEnableHoldTime;
    this.highToEnableSlewTime = highToEnableSlewTime;
    this.lowToEnableHoldTime = lowToEnableHoldTime;
    this.lowToEnableSlewTime = lowToEnableSlewTime;

    validateVoltages();
    validateSlewTimes();
  }

  protected void setPropagationTime(int highToLowPropagation, int lowToHighPropagation)
  {
    this.highToLowHoldTime = (int) Math.round(highToLowPropagation / 2.0);
    this.highToLowSlewTime = highToLowPropagation;
    this.lowToHighHoldTime = (int) Math.round(lowToHighPropagation / 2.0);
    this.lowToHighSlewTime = lowToHighPropagation;
  }

  protected void setEnableTime(int highToLowEnable, int lowToHighEnable)
  {
    this.highToEnableHoldTime = (int) Math.round(highToLowEnable / 2.0);
    this.highToEnableSlewTime = highToLowEnable;
    this.lowToEnableHoldTime = (int) Math.round(lowToHighEnable / 2.0);
    this.lowToEnableSlewTime = lowToHighEnable;
  }

  protected void setDisableTime(int highToLowDisable, int lowToHighDisable)
  {
    this.highToDisableHoldTime = (int) Math.round(highToLowDisable / 2.0);
    this.highToDisableSlewTime = highToLowDisable;
    this.lowToDisableHoldTime = (int) Math.round(lowToHighDisable / 2.0);
    this.lowToDisableSlewTime = lowToHighDisable;
  }

  protected void validateVoltages()
  {
    if (this.highVoltageOut <= this.lowVoltageOut)
    {
      throw new SimulatorException("High voltage out must be greater than low voltage out.");
    }

    if (this.highVoltageIn <= this.lowVoltageIn)
    {
      throw new SimulatorException("High voltage in must be greater than low voltage out.");
    }
  }

  protected void validateSlewTimes()
  {
    if (highToLowSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }

    if (lowToHighSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }

    if (highToDisableSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }

    if (lowToDisableSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }

    if (highToEnableSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }

    if (lowToEnableSlewTime <= 0)
    {
      throw new SimulatorException("Slew time must be greater than zero.");
    }
  }

  protected float getVoltsPerTime(int time)
  {
    return (highVoltageOut - lowVoltageOut) / time;
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

  public long calculateHoldTime(float outVoltage, float portVoltage)
  {
    float startVoltage = calculateStartVoltage(portVoltage);
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
    return holdTime;
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
    return getVoltsPerTime(lowToHighSlewTime);
  }

  public float getVoltsPerTimeHighToLow()
  {
    return getVoltsPerTime(highToLowSlewTime);
  }

  public float getVoltageOut(boolean value)
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

  public float getVcc()
  {
    return vcc;
  }

  @Override
  public int compareTo(VoltageConfiguration o)
  {
    return Float.compare(vcc, o.vcc);
  }
}

