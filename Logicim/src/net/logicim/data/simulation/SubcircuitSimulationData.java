package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;

public abstract class SubcircuitSimulationData
    extends ReflectiveData
{
  public long subcircuitSimulationId;
  public long circuitSimulationId;

  public SubcircuitSimulationData()
  {
  }

  public SubcircuitSimulationData(long subcircuitSimulationId, long circuitSimulationId)
  {
    this.subcircuitSimulationId = subcircuitSimulationId;
    this.circuitSimulationId = circuitSimulationId;
  }
}

