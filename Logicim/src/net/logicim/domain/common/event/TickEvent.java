package net.logicim.domain.common.event;

import net.logicim.data.integratedcircuit.event.TickEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.state.State;

public class TickEvent
    extends IntegratedCircuitEvent
{
  public TickEvent(long time, IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline)
  {
    super(timeline.getTime() + time, integratedCircuit, timeline);
  }

  public TickEvent(long time, long id, IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline)
  {
    super(time, id, integratedCircuit, timeline);
  }

  public IntegratedCircuit<? extends Pins, ? extends State> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public TickEventData save()
  {
    return new TickEventData(time, id);
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    IntegratedCircuit<?, ?> integratedCircuit = getIntegratedCircuit();
    integratedCircuit.executeTick(simulation);
  }
}

