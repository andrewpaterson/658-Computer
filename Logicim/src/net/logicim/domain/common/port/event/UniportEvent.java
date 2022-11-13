package net.logicim.domain.common.port.event;

import net.logicim.domain.common.port.Uniport;

public abstract class UniportEvent
    extends PortEvent
{
  protected Uniport port;

  public UniportEvent(Uniport port, long time)
  {
    super(time);
    this.port = port;
  }

  public Uniport getPort()
  {
    return port;
  }
}

