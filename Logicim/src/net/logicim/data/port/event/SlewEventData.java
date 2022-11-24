package net.logicim.data.port.event;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.SlewEvent;

public class SlewEventData
    extends PortOutputEventData<SlewEvent>
{
  protected float startVoltage;
  protected float endVoltage;
  protected long slewTime;

  public SlewEventData(long time, long id, float startVoltage, float endVoltage, long slewTime)
  {
    super(time, id);
    this.startVoltage = startVoltage;
    this.endVoltage = endVoltage;
    this.slewTime = slewTime;
  }

  @Override
  public SlewEvent create(Port port, Timeline timeline)
  {
    return new SlewEvent(port, time, id, startVoltage, endVoltage, slewTime, timeline);
  }
}

