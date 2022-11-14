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
    this.port.add(this);
  }

  public Omniport getPort()
  {
    return port;
  }

  @Override
  public void removeFromOwner()
  {
    port.remove(this);
  }
}

