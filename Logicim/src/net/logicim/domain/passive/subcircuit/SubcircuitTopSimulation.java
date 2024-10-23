package net.logicim.domain.passive.subcircuit;

import net.logicim.data.simulation.SubcircuitTopSimulationData;
import net.logicim.domain.CircuitSimulation;

public class SubcircuitTopSimulation
    extends SubcircuitSimulation
{
  private SubcircuitObject subcircuitObject;

  public SubcircuitTopSimulation(SubcircuitObject subcircuitObject, CircuitSimulation circuitSimulation)
  {
    super(circuitSimulation);
    this.subcircuitObject = subcircuitObject;
  }

  public SubcircuitTopSimulation(SubcircuitObject subcircuitObject,CircuitSimulation circuitSimulation, long id)
  {
    super(circuitSimulation, id);
    this.subcircuitObject = subcircuitObject;
  }

  @Override
  protected String getType()
  {
    return "Top";
  }

  @Override
  public SubcircuitTopSimulationData save(long subcircuitEditorId)
  {
    return new SubcircuitTopSimulationData(getId(),
                                           subcircuitEditorId,
                                           circuitSimulation.getId());
  }

  @Override
  public SubcircuitObject getSubcircuitObject()
  {
    return subcircuitObject;
  }
}

