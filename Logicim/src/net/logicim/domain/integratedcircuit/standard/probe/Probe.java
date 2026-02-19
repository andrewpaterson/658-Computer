package net.logicim.domain.integratedcircuit.standard.probe;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

import static net.logicim.domain.common.LongTime.frequencyToTime;

public class Probe
    extends IntegratedCircuit<ProbePins, Stateless>
{
  public static final String TYPE = "Probe";

  public Probe(SubcircuitSimulation containingSubcircuitSimulation,
               String name,
               ProbePins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
  }

  @Override
  public void executeTick(Simulation simulation)
  {
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

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }
}

