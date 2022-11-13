package net.logicim.domain.common.port.event;

import net.logicim.domain.common.Event;

public abstract class PortEvent
    extends Event
{
  public PortEvent(long time)
  {
    super(time);
  }
}

