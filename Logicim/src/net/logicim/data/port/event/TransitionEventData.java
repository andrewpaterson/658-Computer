package net.logicim.data.port.event;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.event.TransitionEvent;

public class TransitionEventData
    extends PortInputEventData<TransitionEvent>
{
  protected float voltage;

  public TransitionEventData()
  {
  }

  public TransitionEventData(long time, long id, float voltage)
  {
    super(time, id);
    this.voltage = voltage;
  }

  @Override
  public TransitionEvent create(LogicPort port, Timeline timeline)
  {
    return new TransitionEvent(port, voltage, time, id, timeline);
  }
}

