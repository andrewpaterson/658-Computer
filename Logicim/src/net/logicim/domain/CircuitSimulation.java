package net.logicim.domain;

import net.common.util.StringUtil;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Described;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;

public class CircuitSimulation
    implements Described
{
  public static long nextId = 1L;

  protected Circuit circuit;
  protected Simulation simulation;
  protected long id;
  protected String name;
  protected SubcircuitTopSimulation subcircuitTopSimulation;

  public CircuitSimulation(String name)
  {
    this(nextId++, name);
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

  public static void resetNextId()
  {
    nextId = 1;
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

  @Override
  public String toString()
  {
    return getDescription();
  }

  public void setTopSimulation(SubcircuitTopSimulation subcircuitTopSimulation)
  {
    this.subcircuitTopSimulation = subcircuitTopSimulation;
  }
}

