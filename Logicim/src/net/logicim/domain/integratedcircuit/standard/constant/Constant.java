package net.logicim.domain.integratedcircuit.standard.constant;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class Constant
    extends IntegratedCircuit<ConstantPins, ConstantState>
{
  public static final String TYPE = "Constant";

  protected int propagationTime;
  protected long defaultValue;

  public Constant(SubcircuitSimulation containingSubcircuitSimulation,
                  String name,
                  ConstantPins pins,
                  int propagationTime,
                  int defaultValue)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
    this.propagationTime = propagationTime;
    this.defaultValue = defaultValue;
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    throw new SimulatorException("Input transition not allowed on Constant.");
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    if (isPowered(simulation.getTime()))
    {
      ConstantState state = getState();
      Timeline timeline = simulation.getTimeline();
      long constantValue = state.getConstantValue();
      LogicPort output = pins.getOutput();
      output.writeBool(timeline, (constantValue & 1) == 1);
    }
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public State createState()
  {
    return new ConstantState(defaultValue);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    new TickEvent(propagationTime, this, simulation.getTimeline());
  }
}

