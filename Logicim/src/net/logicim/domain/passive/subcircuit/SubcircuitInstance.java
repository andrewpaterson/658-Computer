package net.logicim.domain.passive.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubcircuitInstance
    extends Passive
{
  public static long nextId = 1L;

  protected Map<String, List<TracePort>> namedPins;
  protected String subcircuitTypeName;
  protected String comment;
  protected long id;  //Doesn't seem to be required.
  protected SubcircuitInstanceSimulation subcircuitSimulation;

  public SubcircuitInstance(SubcircuitSimulation containingSubcircuitSimulation,
                            String name,
                            String subcircuitTypeName,
                            String comment)
  {
    super(containingSubcircuitSimulation.getCircuit(), name);
    this.namedPins = new LinkedHashMap<>();
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.subcircuitSimulation = new SubcircuitInstanceSimulation(containingSubcircuitSimulation.getCircuitSimulation(), this);

    updateId(nextId++);
  }

  public SubcircuitInstance(Circuit circuit,
                            SubcircuitInstanceSimulation subcircuitSimulation,
                            String name,
                            String subcircuitTypeName,
                            String comment,
                            long id)
  {
    super(circuit, name);
    this.namedPins = new LinkedHashMap<>();
    this.subcircuitTypeName = subcircuitTypeName;
    this.comment = comment;
    this.subcircuitSimulation = subcircuitSimulation;

    updateId(id);
  }

  protected void updateId(long id)
  {
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  @Override
  public String getType()
  {
    return "Subcircuit instance";
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  public void addTracePorts(String pinName, List<TracePort> tracePorts)
  {
    if (namedPins.get(pinName) == null)
    {
      namedPins.put(pinName, tracePorts);
    }
    else
    {
      throw new SimulatorException("Ports for pin named [%s] have already been added.", pinName);
    }
  }

  public List<TracePort> getTracePorts(String pinName)
  {
    List<TracePort> tracePorts = namedPins.get(pinName);
    if (tracePorts != null)
    {
      return tracePorts;
    }
    else
    {
      throw new SimulatorException("Cannot get ports for pin named [%s].", pinName);
    }
  }

  public SubcircuitInstanceSimulation getSubcircuitSimulation()
  {
    return subcircuitSimulation;
  }

  public long getId()
  {
    return id;
  }

  public static void resetNextId()
  {
    nextId = 1;
  }
}

