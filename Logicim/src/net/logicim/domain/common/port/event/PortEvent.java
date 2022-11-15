package net.logicim.domain.common.port.event;

import net.logicim.domain.common.Event;
import net.logicim.domain.common.port.Port;

public abstract class PortEvent
    extends Event
{
  protected Port port;

  public PortEvent(Port port, long time)
  {
    super(time);
    this.port = port;
    this.port.add(this);

  }

  public Port getPort()
  {
    return port;
  }

  @Override
  public void removeFromOwner()
  {
    port.remove(this);
  }
}

