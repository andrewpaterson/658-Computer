package net.logicim.domain.common.event;

import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.state.State;

public abstract class IntegratedCircuitEvent
    extends Event
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  public IntegratedCircuitEvent(long time, IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline)
  {
    super(time, timeline);
    this.integratedCircuit = integratedCircuit;
    this.integratedCircuit.add(this);
  }

  public IntegratedCircuitEvent(long time, long id, IntegratedCircuit<?, ?> integratedCircuit, Timeline timeline)
  {
    super(time, id, timeline);
    this.integratedCircuit = integratedCircuit;
    this.integratedCircuit.add(this);
  }

  @Override
  public void execute(Simulation simulation)
  {
    removeFromOwner();
  }

  @Override
  public IntegratedCircuit<?, ?> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public void removeFromOwner()
  {
    super.removeFromOwner();
    integratedCircuit.remove(this);
  }

  @Override
  public abstract IntegratedCircuitEventData<?> save();
}

