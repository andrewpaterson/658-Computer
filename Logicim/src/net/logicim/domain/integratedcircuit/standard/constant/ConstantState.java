package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.state.State;

public class ConstantState
    extends State
{
  protected long constantValue;

  public ConstantState()
  {
    this(0);
  }

  public ConstantState(long constantValue)
  {
    super();
    this.constantValue = constantValue;
  }

  public long getConstantValue()
  {
    return constantValue;
  }
}

