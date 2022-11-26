package net.logicim.data.port.event;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.DriveEvent;

public class DriveEventData
    extends PortOutputEventData<DriveEvent>
{
  protected float voltage;

  public DriveEventData()
  {
  }

  public DriveEventData(long time, long id, float voltage)
  {
    super(time, id);
    this.voltage = voltage;
  }

  @Override
  public DriveEvent create(Port port, Timeline timeline)
  {
    return new DriveEvent(port, time, id, voltage, timeline);
  }
}

