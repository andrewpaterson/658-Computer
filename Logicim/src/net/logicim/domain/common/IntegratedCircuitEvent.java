package net.logicim.domain.common;

import net.logicim.domain.common.state.State;

public abstract class IntegratedCircuitEvent
    extends Event
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  public IntegratedCircuitEvent(long time, IntegratedCircuit<?, ?> integratedCircuit)
  {
    super(time);
    this.integratedCircuit = integratedCircuit;
    this.integratedCircuit.add(this);
  }

  @Override
  public void removeFromOwner()
  {
    integratedCircuit.remove(this);
  }
}
