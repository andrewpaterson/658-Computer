package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;

public abstract class SubcircuitSimulationData
    extends ReflectiveData
{
  public long subcircuitSimulationId;
  public long subcircuitEditorId;
  public long circuitSimulationId;

  public SubcircuitSimulationData()
  {
  }

  public SubcircuitSimulationData(long subcircuitSimulationId, long subcircuitEditorId, long circuitSimulationId)
  {
    this.subcircuitSimulationId = subcircuitSimulationId;
    this.subcircuitEditorId = subcircuitEditorId;
    this.circuitSimulationId = circuitSimulationId;
  }
}

