package net.logicim.domain.passive.subcircuit;

import net.logicim.data.simulation.SubcircuitSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;

public abstract class SubcircuitSimulation
{
  public static long nextId = 1L;

  protected CircuitSimulation circuitSimulation;
  protected long id;

  public SubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    this(circuitSimulation, nextId++);
  }

  public SubcircuitSimulation(CircuitSimulation circuitSimulation, long id)
  {
    this.circuitSimulation = circuitSimulation;

    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public Circuit getCircuit()
  {
    return circuitSimulation.getCircuit();
  }

  public long getId()
  {
    return id;
  }

  public String getDescription()
  {
    return "Type [" + getType() + "] ID [" + id + "], CircuitSimulation [" + circuitSimulation.getDescription() + "]";
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

  public void setId(long id)
  {
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  protected abstract String getType();

  public abstract SubcircuitSimulationData save(long subcircuitEditorId);
}

