package net.logicim.domain.passive.subcircuit;

import net.logicim.common.SimulatorException;
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
    List<SubcircuitSimulation> subcircuitSimulations = simulations.get(subcircuitSimulation.getCircuitSimulation());
    if (subcircuitSimulations == null)
    {
      throw new SimulatorException("Could not remove Subcircuit Simulation [%s] it's Circuit Simulation could not be found.", subcircuitSimulation.getDescription());
    }
    boolean removed = subcircuitSimulations.remove(subcircuitSimulation);
    if (!removed)
    {
      throw new SimulatorException("Could not remove Subcircuit Simulation [%s] from Circuit Simulation [%s].", subcircuitSimulation.getDescription(), subcircuitSimulation.getCircuitSimulation().getDescription());
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

  public void validate(List<CircuitInstanceView> circuitInstanceViews)
  {
    Collection<SubcircuitTopSimulation> subcircuitTopSimulations = getSubcircuitTopSimulations();
    for (SubcircuitTopSimulation subcircuitTopSimulation : subcircuitTopSimulations)
    {
      validateSubcircuitTopSimulation(subcircuitTopSimulation, circuitInstanceViews);
    }

  }

  protected void validateSubcircuitTopSimulation(SubcircuitTopSimulation subcircuitTopSimulation, List<CircuitInstanceView> circuitInstanceViews)
  {
    CircuitSimulation circuitSimulation = subcircuitTopSimulation.getCircuitSimulation();
    int depth = 0;
    for (CircuitInstanceView circuitInstanceView : circuitInstanceViews)
    {
      List<SubcircuitSimulation> innerSubcircuitSimulations = circuitInstanceView.getInnerSubcircuitSimulations(circuitSimulation);
      for (SubcircuitSimulation innerSubcircuitSimulation : innerSubcircuitSimulations)
      {
        if (innerSubcircuitSimulation instanceof SubcircuitTopSimulation)
        {
          throw new SimulatorException("Expected only instance subcircuit simulations in inner simulations.");
        }
      }

      if (innerSubcircuitSimulations.size() == 0 && depth > 0)
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
}

