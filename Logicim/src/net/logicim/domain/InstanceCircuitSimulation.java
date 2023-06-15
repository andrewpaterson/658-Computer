package net.logicim.domain;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;

public class InstanceCircuitSimulation
{
  protected CircuitSimulation circuitSimulation;
  protected SubcircuitInstance subcircuitInstance;

  public InstanceCircuitSimulation(CircuitSimulation circuitSimulation, SubcircuitInstance subcircuitInstance)
  {
    this.circuitSimulation = circuitSimulation;
    this.subcircuitInstance = subcircuitInstance;
  }

  public Circuit getCircuit()
  {
    return circuitSimulation.getCircuit();
  }

  public long getId()
  {
    return circuitSimulation.getId();
  }

  public String getDescription()
  {
    return circuitSimulation.getDescription();
  }

  public Simulation getSimulation()
  {
    return circuitSimulation.getSimulation();
  }

  public long getTime()
  {
    return circuitSimulation.getTime();
  }

  public Timeline getTimeline()
  {
    return circuitSimulation.getTimeline();
  }

  public String getName()
  {
    return circuitSimulation.getName();
  }

  public void runSimultaneous()
  {
    circuitSimulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    circuitSimulation.runToTime(timeForward);
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }

  public SubcircuitInstance getSubcircuitInstance()
  {
    return subcircuitInstance;
  }
}

