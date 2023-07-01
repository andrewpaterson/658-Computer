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

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                      long id,
                                      SubcircuitInstance subcircuitInstance)
  {
    super(circuitSimulation, id);
    this.subcircuitInstance = subcircuitInstance;
  }

  @Override
  public SubcircuitInstanceSimulationData save(long subcircuitEditorId)
  {
    return new SubcircuitInstanceSimulationData(getId(),
                                                subcircuitEditorId,
                                                circuitSimulation.getId(),
                                                subcircuitInstance.getId());
  }
}

