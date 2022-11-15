package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;

public class Constant
    extends IntegratedCircuit<ConstantPins, ConstantState>
{
  protected int propagationTime;
  protected long defaultValue;

  public Constant(Circuit circuit, String name, ConstantPins pins, int propagationTime, int defaultValue)
  {
    super(circuit, name, pins);
    this.propagationTime = propagationTime;
    this.defaultValue = defaultValue;
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {
    throw new SimulatorException("Input transition not allowed on Constant.");
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    ConstantState state = getState();
    Timeline timeline = simulation.getTimeline();
    long constantValue = state.getConstantValue();
    Port output = pins.getOutput();
    output.writeBool(timeline, (constantValue & 1) == 1);
  }

  @Override
  public String getType()
  {
    return "Constant";
  }

  @Override
  public State simulationStarted(Simulation simulation)
  {
    simulation.getTimeline().createTickEvent(propagationTime, this);

    return new ConstantState(this, defaultValue);
  }
}

