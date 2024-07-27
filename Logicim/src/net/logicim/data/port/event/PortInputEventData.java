package net.logicim.data.port.event;

import net.logicim.domain.common.port.event.PortInputEvent;

public abstract class PortInputEventData<E extends PortInputEvent>
    extends PortEventData<E>
{
  public PortInputEventData()
  {
  }

  public PortInputEventData(long time, long id)
  {
    super(time, id);
  }
}

