package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.state.State;

public class TickEvent
    extends Event
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  public TickEvent(long time, IntegratedCircuit<?, ?> integratedCircuit)
  {
    super(time);
    this.integratedCircuit = integratedCircuit;
  }

  public IntegratedCircuit<? extends Pins, ? extends State> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public void execute(Simulation simulation)
  {
    IntegratedCircuit<?, ?> integratedCircuit = getIntegratedCircuit();
    integratedCircuit.clockChanged(simulation);
  }
}

