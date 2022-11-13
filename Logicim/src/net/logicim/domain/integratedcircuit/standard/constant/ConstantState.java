package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.state.State;

public class ConstantState
    extends State
{
  protected int constantValue;

  public ConstantState(IntegratedCircuit<ConstantPins, ConstantState> parent, int constantValue)
  {
    super(parent);
    this.constantValue = constantValue;
  }
}

