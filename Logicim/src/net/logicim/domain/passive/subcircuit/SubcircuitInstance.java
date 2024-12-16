package net.logicim.domain.passive.subcircuit;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubcircuitInstance
    extends Passive
{
  protected Map<String, List<TracePort>> namedPins;
  protected SubcircuitInstanceSimulation subcircuitInstanceSimulation;

  public SubcircuitInstance(SubcircuitSimulation containingSubcircuitSimulation, String name)
  {
    super(containingSubcircuitSimulation, name);
    this.namedPins = new LinkedHashMap<>();
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

  public SubcircuitInstanceSimulation getSubcircuitInstanceSimulation()
  {
    return subcircuitInstanceSimulation;
  }

  public void setSubcircuitInstanceSimulation(SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    this.subcircuitInstanceSimulation = subcircuitInstanceSimulation;
  }
}

