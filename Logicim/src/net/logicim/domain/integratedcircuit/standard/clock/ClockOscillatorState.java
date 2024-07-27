package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.state.State;

public class ClockOscillatorState
    extends State
{
  protected boolean state;

  public ClockOscillatorState()
  {
    super();
  }

  public ClockOscillatorState(boolean state)
  {
    super();
    this.state = state;
  }

  public void tick()
  {
    state = !state;
  }

  public boolean getState()
  {
    return state;
  }
}

