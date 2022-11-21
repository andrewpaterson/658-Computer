package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.VoltageConfiguration;

import java.util.List;

public class SlewEvent
    extends PortOutputEvent
{
  protected float startVoltage;  // @ time
  protected float endVoltage;    // @ time + slewTime
  protected long slewTime;

  public SlewEvent(Port port, float endVoltage, long time)
  {
    super(port, time);
    this.startVoltage = Float.NaN;
    this.endVoltage = endVoltage;
    this.slewTime = -1;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    port.slewEvent(simulation, this);
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
    VoltageConfiguration voltageConfiguration = port.getVoltageConfiguration();

    long nowTime = timeline.getTime();
    startVoltage = voltageConfiguration.calculateStartVoltage(calculateVoltageAtTime(nowTime, calculateStartVoltage(nowTime, voltageConfiguration)));

    float voltageDiff = endVoltage - startVoltage;

    float voltsPerTime;
    if (voltageDiff > 0)
    {
      voltsPerTime = voltageConfiguration.getVoltsPerTimeLowToHigh();
      slewTime = (long) (voltageDiff / voltsPerTime);
    }
    else if (voltageDiff < 0)
    {
      voltsPerTime = voltageConfiguration.getVoltsPerTimeHighToLow();
      slewTime = -(long) (voltageDiff / voltsPerTime);
    }
    else
    {
      slewTime = 1;
    }

    long endTime = getEndTime();
    List<PortOutputEvent> driveEvents = port.getOverlappingEvents(endTime);
    if (driveEvents.size() > 0)
    {
      for (PortOutputEvent driveEvent : driveEvents)
      {
        if (driveEvent instanceof DriveEvent)
        {
          timeline.remove(driveEvent);
        }
      }
    }

    return timeline.createPortDriveEvent(port, this);
  }

  protected float calculateStartVoltage(long nowTime, VoltageConfiguration voltageConfiguration)
  {
    float voltage = port.getVoltage(nowTime);
    if (!Float.isNaN(voltage))
    {
      return voltage;
    }
    return voltageConfiguration.getMidVoltageOut();
  }

  public String toDebugString()
  {
    return port.toDebugString();
  }

  public boolean forTime(long time)
  {
    return time <= getEndTime() && time >= this.time;
  }
}

