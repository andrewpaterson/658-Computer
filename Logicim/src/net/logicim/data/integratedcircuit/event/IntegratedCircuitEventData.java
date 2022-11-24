package net.logicim.data.integratedcircuit.event;

import net.logicim.data.common.EventData;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.IntegratedCircuitEvent;
import net.logicim.domain.common.Timeline;

public abstract class IntegratedCircuitEventData<E extends IntegratedCircuitEvent>
    extends EventData<E>
{
  public IntegratedCircuitEventData(long time, long id)
  {
    super(time, id);
  }

  public abstract E create(IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline);
}

