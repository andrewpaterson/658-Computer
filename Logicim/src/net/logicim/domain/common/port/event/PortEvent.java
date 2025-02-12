package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.PortEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.common.port.LogicPort;

public abstract class PortEvent
    extends Event
{
  protected LogicPort port;

  public PortEvent(LogicPort port,
                   long time,
                   Timeline timeline)
  {
    super(time, timeline);
    this.port = port;
    this.port.add(this);
  }

  public PortEvent(LogicPort port,
                   long time,
                   long id,
                   Timeline timeline)
  {
    super(time, timeline, id);
    this.port = port;
    this.port.add(this);
  }

  public LogicPort getPort()
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
    super.removeFromOwner();
    port.remove(this);
  }

  public String toPortOwnerString()
  {
    if (port != null)
    {
      Pins pins = port.getPins();
      if (pins != null)
      {
        Component component = pins.getComponent();
        if (component != null)
        {
          return component.getDescription();
        }
      }
    }
    return "";
  }

  @Override
  public abstract PortEventData<?> save();
}

