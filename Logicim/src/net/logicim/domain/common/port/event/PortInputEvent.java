package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortInputEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public abstract class PortInputEvent
    extends PortEvent
{
  public PortInputEvent(Port port, long time)
  {
    super(port, time);
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
  }

  @Override
  public abstract PortInputEventData save();
}

