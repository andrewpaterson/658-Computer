package net.logicim.domain.common.state;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;

public class State
{
  IntegratedCircuit<? extends Pins, ? extends State> parent;

  public State(IntegratedCircuit<? extends Pins, ? extends State> parent)
  {
    this.parent = parent;
  }

  public IntegratedCircuit<? extends Pins, ? extends State> getParent()
  {
    return parent;
  }

  public boolean isStateless()
  {
    return false;
  }
}
