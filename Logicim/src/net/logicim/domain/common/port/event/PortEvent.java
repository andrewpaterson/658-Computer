package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.PortEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Event;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;

public abstract class PortEvent
    extends Event
{
  protected Port port;

  public PortEvent(Port port, long time, Timeline timeline)
  {
    super(time, timeline);
    this.port = port;
    this.port.add(this);
  }

  public PortEvent(Port port, long time, long id, Timeline timeline)
  {
    super(time, id, timeline);
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

  @Override
  public abstract PortEventData<?> save();
}

