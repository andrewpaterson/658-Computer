package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;

public class Oscilloscope
    extends IntegratedCircuit<OscilloscopePins, OscilloscopeState>
{
  public Oscilloscope(Circuit circuit, String name, OscilloscopePins pins)
  {
    super(circuit, name, pins);
  }

  @Override
  public State createState(Simulation simulation)
  {
    return null;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {

  }

  @Override
  public String getType()
  {
    return null;
  }
}

