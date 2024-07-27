package net.logicim.data.integratedcircuit.event;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.TickEvent;

public class TickEventData
    extends IntegratedCircuitEventData<TickEvent>
{
  public TickEventData()
  {
  }

  public TickEventData(long time, long id)
  {
    super(time, id);
  }

  @Override
  public TickEvent create(IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline)
  {
    return new TickEvent(time, id, integratedCircuit, timeline);
  }
}

