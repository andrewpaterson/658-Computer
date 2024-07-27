package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.PortInputEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;

public abstract class PortInputEvent
    extends PortEvent
{
  public PortInputEvent(LogicPort port, long time, Timeline timeline)
  {
    super(port, time, timeline);
  }

  public PortInputEvent(LogicPort port, long time, long id, Timeline timeline)
  {
    super(port, time, id, timeline);
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
  }

  @Override
  public abstract PortInputEventData<?> save();
}

