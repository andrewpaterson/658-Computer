package net.logicim.domain.common.port.event;

import net.common.SimulatorException;
import net.logicim.data.port.event.SlewEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;
import net.logicim.domain.common.voltage.Voltage;

import java.util.List;

public class SlewEvent
    extends PortOutputEvent
{
  protected float startVoltage;  // @ time
  protected float endVoltage;    // @ time + slewTime
  protected long slewTime;

  public SlewEvent(LogicPort port, float endVoltage, long time, Timeline timeline)
  {
    super(port, timeline.getTime() + time, timeline);
    this.startVoltage = Float.NaN;
    this.endVoltage = endVoltage;
    this.slewTime = -1;
  }

  public SlewEvent(LogicPort port, long time, long id, float startVoltage, float endVoltage, long slewTime, Timeline timeline)
  {
    super(port, time, id, timeline);
    this.startVoltage = startVoltage;
    this.endVoltage = endVoltage;
    this.slewTime = slewTime;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    port.slewEvent(simulation, this);
  }

  @Override
  public SlewEventData save()
  {
    return new SlewEventData(time, id, startVoltage, endVoltage, slewTime);
  }

  public long getEndTime()
  {
    return time + slewTime;
  }

  public float getStartVoltage()
  {
    return startVoltage;
  }

  public long getSlewTime()
  {
    return slewTime;
  }

  public float getEndVoltage()
  {
    return endVoltage;
  }

  public float calculateVoltageAtTime(long time, float startVoltage)
  {
    long l = time - this.time;
    float fractionStart = l / (float) slewTime;
    return fractionStart * endVoltage + (1 - fractionStart) * startVoltage;
  }

  public float getVoltage(long time)
  {
    if (forTime(time))
    {
      return calculateVoltageAtTime(time, startVoltage);
    }
    else
    {
      return Float.NaN;
    }
  }

  public DriveEvent update(Timeline timeline)
  {
    VoltageConfigurationSource voltageConfiguration = port.getVoltageConfigurationSource();
    float vcc = port.getVCC(time);

    long nowTime = timeline.getTime();
    startVoltage = voltageConfiguration.calculateStartVoltage(calculateVoltageAtTime(nowTime, calculateStartVoltage(nowTime, voltageConfiguration, vcc)), vcc);

    slewTime = calculateSlewTime(voltageConfiguration, vcc);
    if (slewTime != Long.MAX_VALUE)
    {
      if (slewTime < 1)
      {
        throw new SimulatorException("Slew time must be in the future.");
      }

      long startTime = getTime();
      List<DriveEvent> driveEvents = port.getFutureDriveEvents(startTime);
      if (driveEvents.size() > 0)
      {
        timeline.removeAll(driveEvents);
      }

      return new DriveEvent(port, getSlewTime(), getEndVoltage(), timeline);
    }
    else
    {
      return null;
    }
  }

  long calculateSlewTime(VoltageConfigurationSource voltageConfiguration, float vcc)
  {
    float voltageDiff = endVoltage - startVoltage;

    float voltsPerTime;
    if (voltageDiff > 0)
    {
      voltsPerTime = voltageConfiguration.getVoltsPerTimeLowToHigh(vcc);
      if (!Float.isInfinite(voltsPerTime))
      {
        return (long) (voltageDiff / voltsPerTime);
      }
      else
      {
        return Long.MAX_VALUE;
      }
    }
    else if (voltageDiff < 0)
    {
      voltsPerTime = voltageConfiguration.getVoltsPerTimeHighToLow(vcc);
      if (!Float.isInfinite(voltsPerTime))
      {
        return -(long) (voltageDiff / voltsPerTime);
      }
      else
      {
        return Long.MAX_VALUE;
      }
    }
    else
    {
      return 1;
    }
  }

  protected float calculateStartVoltage(long nowTime, VoltageConfigurationSource voltageConfiguration, float vcc)
  {
    float voltage = port.getVoltageOut(nowTime);
    if (!Float.isNaN(voltage))
    {
      return voltage;
    }
    return voltageConfiguration.getVoltageOut(false, vcc);
  }

  public String toDebugString()
  {
    return port.toDebugString();
  }

  public boolean forTime(long time)
  {
    return time <= getEndTime() && time >= this.time;
  }

  @Override
  public String toShortString()
  {
    return super.toShortString() + " " + Voltage.toVoltageString(startVoltage) + " -> " + Voltage.toVoltageString(endVoltage);
  }
}

