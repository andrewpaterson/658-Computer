package net.logicim.domain.passive.subcircuit;

import net.logicim.data.simulation.SubcircuitTopSimulationData;
import net.logicim.domain.CircuitSimulation;

public class SubcircuitTopSimulation
    extends SubcircuitSimulation
{
  public SubcircuitTopSimulation(CircuitSimulation circuitSimulation)
  {
    super(circuitSimulation);
  }

  public SubcircuitTopSimulation(CircuitSimulation circuitSimulation, long id)
  {
    super(circuitSimulation, id);
  }

  @Override
  protected String getType()
  {
    return "Top";
  }

  @Override
  public SubcircuitTopSimulationData save(long subcircuitEditorId, long viewPathId)
  {
    return new SubcircuitTopSimulationData(viewPathId,
                                           subcircuitEditorId,
                                           circuitSimulation.getId(),
                                           getId());
  }

  @Override
  public boolean isTop()
  {
    return true;
  }
}

