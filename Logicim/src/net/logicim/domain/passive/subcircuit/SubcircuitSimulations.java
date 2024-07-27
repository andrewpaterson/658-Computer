package net.logicim.domain.passive.subcircuit;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;

import java.util.*;

public class SubcircuitSimulations
{
  protected Map<CircuitSimulation, List<SubcircuitSimulation>> simulations;

  public SubcircuitSimulations()
  {
    this.simulations = new LinkedHashMap<>();
  }

  public void add(SubcircuitSimulation subcircuitSimulation)
  {
    List<SubcircuitSimulation> subcircuitSimulations = simulations.get(subcircuitSimulation.circuitSimulation);
    if (subcircuitSimulations != null)
    {
      if (subcircuitSimulations.contains(subcircuitSimulation))
      {
        throw new SimulatorException("A simulation [%s] for circuit [%s] already exists.", subcircuitSimulation.getDescription(), subcircuitSimulation.getCircuitSimulation().getDescription());
      }
    }
    else
    {
      subcircuitSimulations = new ArrayList<>();
      simulations.put(subcircuitSimulation.circuitSimulation, subcircuitSimulations);
    }

    subcircuitSimulations.add(subcircuitSimulation);
  }

  public void remove(SubcircuitSimulation subcircuitSimulation)
  {
    CircuitSimulation circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    List<SubcircuitSimulation> subcircuitSimulations = simulations.get(circuitSimulation);
    if (subcircuitSimulations == null)
    {
      throw new SimulatorException("Could not remove Subcircuit Simulation [%s] it's Circuit Simulation could not be found.", subcircuitSimulation.getDescription());
    }
    boolean removed = subcircuitSimulations.remove(subcircuitSimulation);
    if (!removed)
    {
      throw new SimulatorException("Could not remove Subcircuit Simulation [%s] from Circuit Simulation [%s].", subcircuitSimulation.getDescription(), circuitSimulation.getDescription());
    }

    if (subcircuitSimulations.isEmpty())
    {
      simulations.remove(circuitSimulation);
    }
  }

  public List<SubcircuitSimulation> getSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    return simulations.get(circuitSimulation);
  }

  public Collection<SubcircuitTopSimulation> getSubcircuitTopSimulations()
  {
    ArrayList<SubcircuitTopSimulation> subcircuitTopSimulations = new ArrayList<>();
    for (List<SubcircuitSimulation> subcircuitSimulations : simulations.values())
    {
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        if (subcircuitSimulation instanceof SubcircuitTopSimulation)
        {
          subcircuitTopSimulations.add((SubcircuitTopSimulation) subcircuitSimulation);
        }
      }
    }
    return subcircuitTopSimulations;
  }

  public Collection<SubcircuitInstanceSimulation> getSubcircuitInstanceSimulations()
  {
    ArrayList<SubcircuitInstanceSimulation> subcircuitTopSimulations = new ArrayList<>();
    for (List<SubcircuitSimulation> subcircuitSimulations : simulations.values())
    {
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        if (subcircuitSimulation instanceof SubcircuitInstanceSimulation)
        {
          subcircuitTopSimulations.add((SubcircuitInstanceSimulation) subcircuitSimulation);
        }
      }
    }
    return subcircuitTopSimulations;
  }

  public Collection<SubcircuitSimulation> getSubcircuitSimulations()
  {
    ArrayList<SubcircuitSimulation> result = new ArrayList<>();
    Collection<List<SubcircuitSimulation>> collection = simulations.values();
    for (List<SubcircuitSimulation> subcircuitSimulations : collection)
    {
      result.addAll(subcircuitSimulations);
    }
    return result;
  }

  public Set<CircuitSimulation> getCircuitSimulations()
  {
    return simulations.keySet();
  }

  public void validate(List<CircuitInstanceView> orderedTopDownCircuitInstanceViews)
  {
    Collection<SubcircuitTopSimulation> subcircuitTopSimulations = getSubcircuitTopSimulations();
    if (subcircuitTopSimulations.size() < 1)
    {
      throw new SimulatorException("Expected at least one top subcircuit simulation.");

    }
    for (SubcircuitTopSimulation subcircuitTopSimulation : subcircuitTopSimulations)
    {
      validateSubcircuitTopSimulation(subcircuitTopSimulation, orderedTopDownCircuitInstanceViews);
    }
  }

  protected void validateSubcircuitTopSimulation(SubcircuitTopSimulation subcircuitTopSimulation, List<CircuitInstanceView> orderedTopDownCircuitInstanceViews)
  {
    CircuitSimulation circuitSimulation = subcircuitTopSimulation.getCircuitSimulation();
    int depth = 0;
    for (CircuitInstanceView circuitInstanceView : orderedTopDownCircuitInstanceViews)
    {
      List<SubcircuitInstanceSimulation> innerSubcircuitSimulations = circuitInstanceView.getInnerSubcircuitSimulations(circuitSimulation);

      if (innerSubcircuitSimulations.isEmpty() && depth > 0)
      {
        throw new SimulatorException("Expected at least one instance simulation for circuit simulation[%s] in circuit instance view [%s].", circuitSimulation.getDescription(), circuitInstanceView.getDescription());
      }
      depth++;
    }
  }

  public boolean hasSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation == null)
    {
      return false;
    }

    List<SubcircuitSimulation> subcircuitSimulations = simulations.get(subcircuitSimulation.getCircuitSimulation());
    if (subcircuitSimulations != null)
    {
      return subcircuitSimulations.contains(subcircuitSimulation);
    }
    else
    {
      return false;
    }
  }

  public Map<CircuitSimulation, List<SubcircuitSimulation>> getSimulations()
  {
    return simulations;
  }

  public void clear()
  {
    simulations.clear();
  }
}

