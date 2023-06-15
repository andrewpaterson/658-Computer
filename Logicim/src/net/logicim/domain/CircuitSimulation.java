package net.logicim.domain;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;

public class CircuitSimulation
{
  public static long nextId = 1L;

  protected Circuit circuit;
  protected Simulation simulation;
  protected long id;
  protected String name;

  public CircuitSimulation()
  {
    id = nextId;
    nextId++;

    this.circuit = new Circuit();
    this.simulation = new Simulation();
    this.name = "";
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

  public InstanceCircuitSimulation reset(SubcircuitInstance subcircuitInstance)
  {
    simulation = new Simulation();
    InstanceCircuitSimulation instanceCircuitSimulation = new InstanceCircuitSimulation(this, subcircuitInstance);
    this.circuit.resetSimulation(instanceCircuitSimulation);
    return instanceCircuitSimulation;
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

  public static void resetNextId()
  {
    nextId = 1;
  }
}

