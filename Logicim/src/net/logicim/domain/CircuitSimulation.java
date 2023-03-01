package net.logicim.domain;

import net.logicim.domain.common.Circuit;

public class CircuitSimulation
{
  protected Circuit circuit;
  protected Simulation simulation;

  public CircuitSimulation()
  {
    this.circuit = new Circuit();
    this.simulation = circuit.resetSimulation();
  }

  public CircuitSimulation(Circuit circuit, Simulation simulation)
  {
    this.circuit = circuit;
    this.simulation = simulation;
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  public long getTime()
  {
    return simulation.getTime();
  }

  public void reset()
  {
    simulation = circuit.resetSimulation();
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }
}

