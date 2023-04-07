package net.logicim.domain;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;

public class CircuitSimulation
{
  public static long nextId = 1L;

  protected Circuit circuit;
  protected Simulation simulation;
  protected long id;

  public CircuitSimulation()
  {
    id = nextId;
    nextId++;

    this.circuit = new Circuit();
    this.simulation = new Simulation();
    circuit.resetSimulation(this);
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
    simulation = new Simulation();
    circuit.resetSimulation(this);
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public long getId()
  {
    return id;
  }

  public Timeline getTimeline()
  {
    return simulation.getTimeline();
  }
}

