package net.logicim.domain.common.port.event;

import net.logicim.domain.common.port.Omniport;

public abstract class OmniportEvent
    extends PortEvent
{
  protected Omniport port;

  public OmniportEvent(Omniport port, long time)
  {
    super(time);
    this.port = port;
  }

  public Omniport getPort()
  {
    return port;
  }
}

