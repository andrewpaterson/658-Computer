package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.state.State;

public class OscilloscopeState
    extends State
{
  public OscilloscopeState(IntegratedCircuit<? extends Pins, ? extends State> parent)
  {
    super(parent);
  }
}
