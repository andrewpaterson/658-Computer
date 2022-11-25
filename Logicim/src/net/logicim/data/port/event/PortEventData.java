package net.logicim.data.port.event;

import net.logicim.data.common.event.EventData;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;

public abstract class PortEventData<E extends PortEvent>
    extends EventData<E>
{
  public PortEventData(long time, long id)
  {
    super(time, id);
  }

  public abstract E create(Port port, Timeline timeline);
}

