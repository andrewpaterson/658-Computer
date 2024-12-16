package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.CircuitElement;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.*;

public class ViewPathComponentSimulation<COMPONENT extends CircuitElement>
{
  protected Map<ViewPath, Map<CircuitSimulation, COMPONENT>> map;

  public ViewPathComponentSimulation()
  {
    this.map = new LinkedHashMap<>();
  }

  public void put(ViewPath viewPath,
                  CircuitSimulation circuitSimulation,
                  COMPONENT component)
  {
    Map<CircuitSimulation, COMPONENT> simulationMap = map.get(viewPath);
    if (simulationMap == null)
    {
      simulationMap = new LinkedHashMap<>();
      map.put(viewPath, simulationMap);
    }

    COMPONENT existingComponent = simulationMap.get(circuitSimulation);
    if (existingComponent != null)
    {
      throw new SimulatorException("A Component [%s] for Path [%s] for Simulation [%s] already exists whilst adding Component [%s].",
                                   existingComponent.getDescription(),
                                   viewPath.getDescription(),
                                   circuitSimulation.getDescription(),
                                   component.getDescription());
    }

    simulationMap.put(circuitSimulation, component);
  }

  public COMPONENT get(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    Map<CircuitSimulation, COMPONENT> simulationMap = map.get(viewPath);
    if (simulationMap != null)
    {
      return simulationMap.get(circuitSimulation);
    }
    return null;
  }

  public COMPONENT remove(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    Map<CircuitSimulation, COMPONENT> simulationMap = map.get(viewPath);
    if (simulationMap == null)
    {
      return null;
    }
    COMPONENT component = simulationMap.remove(circuitSimulation);
    if (component == null)
    {
      throw new SimulatorException("Cannot remove Component on Path [%s] for Simulation [%s].  No such Simulation exists.",
                                   viewPath.getDescription(),
                                   circuitSimulation.getDescription());
    }

    if (simulationMap.isEmpty())
    {
      map.remove(viewPath);
    }

    return component;
  }

  public Set<Map.Entry<ViewPath, Map<CircuitSimulation, COMPONENT>>> getEntrySet()
  {
    return map.entrySet();
  }

  public void clear()
  {
    map.clear();
  }

  public Set<? extends SubcircuitSimulation> getSimulations()
  {
    Set<? extends SubcircuitSimulation> result = new LinkedHashSet<>();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, COMPONENT>> pathEntry : map.entrySet())
    {
      ViewPath viewPath = pathEntry.getKey();
      result.addAll((List) viewPath.getSubcircuitSimulations());
    }
    return result;
  }

  public Set<? extends SubcircuitSimulation> getSimulations(CircuitSimulation circuitSimulation)
  {
    Set<SubcircuitSimulation> result = new LinkedHashSet<>();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, COMPONENT>> pathEntry : map.entrySet())
    {
      ViewPath viewPath = pathEntry.getKey();
      result.add(viewPath.getSubcircuitSimulation(circuitSimulation));
    }
    return result;
  }

  public Map<SubcircuitSimulation, COMPONENT> getSubcircuitSimulationComponents(CircuitSimulation circuitSimulation)
  {
    Map<SubcircuitSimulation, COMPONENT> result = new LinkedHashMap<>();

    for (Map.Entry<ViewPath, Map<CircuitSimulation, COMPONENT>> pathEntry : map.entrySet())
    {
      ViewPath viewPath = pathEntry.getKey();
      SubcircuitSimulation subcircuitSimulation = viewPath.getSubcircuitSimulationOrNull(circuitSimulation);
      if (subcircuitSimulation != null)
      {
        Map<CircuitSimulation, COMPONENT> simulationMap = pathEntry.getValue();
        COMPONENT component = simulationMap.get(circuitSimulation);

        result.put(subcircuitSimulation, component);
      }
    }

    return result;
  }

  public COMPONENT getComponentSlow(SubcircuitSimulation subcircuitSimulation)
  {
    CircuitSimulation circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    Map<SubcircuitSimulation, COMPONENT> simulationComponents = getSubcircuitSimulationComponents(circuitSimulation);
    return simulationComponents.get(subcircuitSimulation);
  }

  public String getComponentType()
  {
    for (Map<CircuitSimulation, COMPONENT> simulationMap : map.values())
    {
      for (COMPONENT integratedCircuit : simulationMap.values())
      {
        return integratedCircuit.getType();
      }
    }
    return "";
  }

  public List<COMPONENT> getComponents()
  {
    List<COMPONENT> components = new ArrayList<>();
    for (Map<CircuitSimulation, COMPONENT> simulationMap : map.values())
    {
      components.addAll(simulationMap.values());
    }
    return components;
  }
}

