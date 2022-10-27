package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.state.State;

public class ClockOscillatorState
    extends State
{
  protected boolean state;

  public ClockOscillatorState(IntegratedCircuit<ClockOscillatorPins, ClockOscillatorState> parent)
  {
    super(parent);
    state = true;
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

