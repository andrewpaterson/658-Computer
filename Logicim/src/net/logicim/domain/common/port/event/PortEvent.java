package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Event;
import net.logicim.domain.common.IntegratedCircuit;
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

  public void execute(Simulation simulation)
  {
    removeFromOwner();
  }

  public IntegratedCircuit<?, ?> getIntegratedCircuit()
  {
    return port.getPins().getIntegratedCircuit();
  }

  @Override
  public void removeFromOwner()
  {
    port.remove(this);
  }
}

