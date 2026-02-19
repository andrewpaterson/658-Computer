package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class AndGate
    extends IntegratedCircuit<AndGatePins, Stateless>
{
  public static final String TYPE = "AND Gate";

  public AndLogic logic;

  public AndGate(SubcircuitSimulation containingSubcircuitSimulation,
                 String name,
                 AndGatePins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
    logic = createLogic();
  }

  protected AndLogic createLogic()
  {
    return new AndLogic();
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    List<LogicPort> inputs = pins.getInputs();
    LogicPort output = pins.getOutput();

    logic.inputTransition(timeline, inputs, output);
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public Stateless createState()
  {
    return new Stateless();
  }
}

