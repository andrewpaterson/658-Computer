package net.logicim.domain.passive.subcircuit;

import net.logicim.data.simulation.SubcircuitInstanceSimulationData;
import net.logicim.domain.CircuitSimulation;

public class SubcircuitInstanceSimulation
    extends SubcircuitSimulation
{
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                      SubcircuitInstance subcircuitInstance)
  {
    super(circuitSimulation);
    this.subcircuitInstance = subcircuitInstance;
  }

  @Override
  public SubcircuitInstanceSimulationData save()
  {
    return new SubcircuitInstanceSimulationData(getId(), circuitSimulation.getId(), subcircuitInstance.getId());
  }

  @Override
  public SubcircuitInstance getSubcircuitInstance()
  {
    return subcircuitInstance;
  }
}

