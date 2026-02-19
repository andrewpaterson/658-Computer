package net.logicim.domain.integratedcircuit.standard.counter;

import net.logicim.domain.common.state.State;

public class CounterState
    extends State
{
  protected int state;
  protected boolean terminal;

  public CounterState()
  {
    super();
  }

  public CounterState(int state)
  {
    super();
    this.state = state;
  }

  public int getState()
  {
    return state;
  }

  public void setState(int state)
  {
    this.state = state;
  }

  public void count(int terminalValue)
  {
    if (state >= terminalValue)
    {
      state = 0;
    }
    else
    {
      state++;
    }
  }

  public boolean getTerminal()
  {
    return terminal;
  }

  public void setTerminal(boolean terminal)
  {
    this.terminal = terminal;
  }
}

