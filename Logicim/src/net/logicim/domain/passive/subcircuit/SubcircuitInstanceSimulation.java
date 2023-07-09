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
                                      long id)
  {
    super(circuitSimulation, id);
    this.subcircuitInstance = null;
  }

  @Override
  public SubcircuitInstanceSimulationData save(long subcircuitEditorId)
  {
    return new SubcircuitInstanceSimulationData(getId(),
                                                subcircuitEditorId,
                                                circuitSimulation.getId());
  }

  public void setSubcircuitInstance(SubcircuitInstance subcircuitInstance)
  {
    this.subcircuitInstance = subcircuitInstance;
  }
}

