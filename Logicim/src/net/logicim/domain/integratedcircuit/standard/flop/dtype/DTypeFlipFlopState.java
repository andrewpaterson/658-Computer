package net.logicim.domain.integratedcircuit.standard.flop.dtype;

import net.logicim.domain.common.state.State;

public class DTypeFlipFlopState
    extends State
{
  protected boolean state;

  public DTypeFlipFlopState()
  {
    super();
  }

  public DTypeFlipFlopState(boolean state)
  {
    super();
    this.state = state;
  }

  public boolean getState()
  {
    return state;
  }

  public boolean setState(boolean state)
  {
    if (state != this.state)
    {
      this.state = state;
      return true;
    }
    else
    {
      return false;
    }
  }
}

