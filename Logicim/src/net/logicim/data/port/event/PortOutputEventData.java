package net.logicim.data.port.event;

import net.logicim.domain.common.port.event.PortOutputEvent;

public abstract class PortOutputEventData<E extends PortOutputEvent>
    extends PortEventData<E>
{
  public PortOutputEventData()
  {
  }

  public PortOutputEventData(long time, long id)
  {
    super(time, id);
  }
}

