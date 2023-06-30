package net.logicim.domain.passive.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;

import java.util.*;

public class SubcircuitSimulations
{
  protected Map<CircuitSimulation, SubcircuitSimulation> simulations;

  public SubcircuitSimulations()
  {
    this.simulations = new LinkedHashMap<>();
  }

  public void add(SubcircuitSimulation subcircuitSimulation)
  {
    SubcircuitSimulation existing = simulations.get(subcircuitSimulation.circuitSimulation);
    if (existing != null)
    {
      throw new SimulatorException("A simulation [%s] for circuit [%s] already exists.", existing.getDescription(), subcircuitSimulation.getCircuitSimulation().getDescription());
    }

    simulations.put(subcircuitSimulation.circuitSimulation, subcircuitSimulation);
  }

  public void remove(CircuitSimulation circuitSimulation)
  {
    simulations.remove(circuitSimulation);
  }

  public SubcircuitSimulation get(CircuitSimulation circuitSimulation)
  {
    return simulations.get(circuitSimulation);
  }

  public Collection<SubcircuitTopSimulation> getSubcircuitTopSimulations()
  {
    ArrayList<SubcircuitTopSimulation> subcircuitTopSimulations = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : simulations.values())
    {
      if (subcircuitSimulation instanceof SubcircuitTopSimulation)
      {
        subcircuitTopSimulations.add((SubcircuitTopSimulation) subcircuitSimulation);
      }
    }
    return subcircuitTopSimulations;
  }

  public Collection<SubcircuitSimulation> getSubcircuitSimulations()
  {
    return simulations.values();
  }

  public Set<CircuitSimulation> getCircuitSimulations()
  {
    return simulations.keySet();
  }
}

