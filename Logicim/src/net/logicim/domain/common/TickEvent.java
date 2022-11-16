package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.state.State;

public class TickEvent
    extends IntegratedCircuitEvent
{
  public TickEvent(long time, IntegratedCircuit<?, ?> integratedCircuit)
  {
    super(time, integratedCircuit);
  }

  public IntegratedCircuit<? extends Pins, ? extends State> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    IntegratedCircuit<?, ?> integratedCircuit = getIntegratedCircuit();
    integratedCircuit.executeTick(simulation);
  }
}

