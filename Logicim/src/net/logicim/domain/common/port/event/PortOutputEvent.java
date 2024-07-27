package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.PortOutputEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;

public abstract class PortOutputEvent
    extends PortEvent
{
  public PortOutputEvent(LogicPort port, long time, Timeline timeline)
  {
    super(port, time, timeline);
  }

  public PortOutputEvent(LogicPort port, long time, long id, Timeline timeline)
  {
    super(port, time, id, timeline);
  }

  public abstract float getVoltage(long time);

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
  }

  @Override
  public abstract PortOutputEventData<?> save();
}

