package net.logicim.domain.common.state;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;

public class Stateless
    extends State
{
  public Stateless(IntegratedCircuit<? extends Pins, ? extends State> parent)
  {
    super(parent);
  }

  @Override
  public boolean isStateless()
  {
    return true;
  }
}

