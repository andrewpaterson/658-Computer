package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;

public class Constant
    extends IntegratedCircuit<ConstantPins, ConstantState>
{
  protected int propagationTime;
  protected int defaultValue;

  public Constant(Circuit circuit, String name, ConstantPins pins, int propagationTime, int defaultValue)
  {
    super(circuit, name, pins);
    this.propagationTime = propagationTime;
    this.defaultValue = defaultValue;
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {
    throw new SimulatorException("Input transition not allowed on Contstant.");
  }

  @Override
  public String getType()
  {
    return null;
  }

  @Override
  public State simulationStarted(Simulation simulation)
  {
    simulation.getTimeline().createTickEvent(propagationTime, this);

    return new ConstantState(this, defaultValue);

  }
}

