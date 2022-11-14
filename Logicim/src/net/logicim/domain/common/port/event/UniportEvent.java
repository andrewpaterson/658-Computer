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
    this.port.add(this);
  }

  public Uniport getPort()
  {
    return port;
  }
  @Override
  public void removeFromOwner()
  {
    port.remove(this);
  }
}

