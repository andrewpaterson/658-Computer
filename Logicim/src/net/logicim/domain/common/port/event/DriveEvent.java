package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.DriveEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.common.port.Port;

public class DriveEvent
    extends PortOutputEvent
{
  protected float voltage;

  public DriveEvent(Port port, long time, float voltage, Timeline timeline)
  {
    super(port, timeline.getTime() + time, timeline);
    this.voltage = voltage;
  }

  public DriveEvent(Port port, long time, long id, float voltage, Timeline timeline)
  {
    super(port, time, id, timeline);
    this.voltage = voltage;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    port.driveEvent(simulation, this);
  }

  @Override
  public float getVoltage(long time)
  {
    if (time >= this.time)
    {
      return voltage;
    }
    else
    {
      return Float.NaN;
    }
  }

  @Override
  public DriveEventData save()
  {
    return new DriveEventData(time, id, voltage);
  }

  @Override
  public String toShortString()
  {
    return super.toShortString() + " " + Voltage.getVoltageString(voltage);
  }
}

