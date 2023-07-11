package net.logicim.domain;

import net.logicim.common.util.StringUtil;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.Collection;

public class CircuitSimulation
{
  public static long nextId = 1L;

  protected Circuit circuit;
  protected Simulation simulation;
  protected long id;
  protected String name;

  public CircuitSimulation()
  {
    this(nextId++, "");
  }

  public CircuitSimulation(long id, String name)
  {
    this.name = name;

    this.circuit = new Circuit();
    this.simulation = new Simulation();

    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
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

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    String name = getName();
    long id = getId();
    if (StringUtil.isEmptyOrNull(name))
    {
      return "" + id;
    }
    else
    {
      return name + " (" + id + ")";
    }
  }

  public void reset(CircuitSimulation circuitSimulation)
  {
    this.simulation = new Simulation();
    this.circuit.resetSimulation(circuitSimulation);
  }

  public CircuitSimulationData save()
  {
    TimelineData timelineData = getTimeline().save();
    return new CircuitSimulationData(id, timelineData, name);
  }

  public static void resetNextId()
  {
    nextId = 1;
  }

  @Override
  public String toString()
  {
    return getDescription();
  }

  public static SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation, Collection<SubcircuitSimulation> subcircuitSimulations)
  {
    for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
    {
      if (subcircuitSimulation.getCircuitSimulation() == circuitSimulation)
      {
        return subcircuitSimulation;
      }
    }
    return null;
  }
}

