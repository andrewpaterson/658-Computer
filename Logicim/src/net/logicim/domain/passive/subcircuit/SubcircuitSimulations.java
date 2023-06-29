package net.logicim.domain.passive.subcircuit;

import net.logicim.domain.CircuitSimulation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SubcircuitSimulations
{
  protected Map<CircuitSimulation, SubcircuitSimulation> simulations;

  public SubcircuitSimulations()
  {
    this.simulations = new LinkedHashMap<>();
  }

  public void add(SubcircuitSimulation subcircuitSimulation)
  {
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

  public Collection<SubcircuitSimulation> getSubcircuitSimulations()
  {
    return simulations.values();
  }

  public Set<CircuitSimulation> getCircuitSimulations()
  {
    return simulations.keySet();
  }
}

