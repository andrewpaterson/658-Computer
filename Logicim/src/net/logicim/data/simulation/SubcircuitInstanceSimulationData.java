package net.logicim.data.simulation;

public class SubcircuitInstanceSimulationData
    extends SubcircuitSimulationData
{
  public long subcircuitInstanceId;

  public SubcircuitInstanceSimulationData()
  {
  }

  public SubcircuitInstanceSimulationData(long subcircuitSimulationId,
                                          long subcircuitEditorId,
                                          long circuitSimulationId,
                                          long subcircuitInstanceId)
  {
    super(subcircuitSimulationId, subcircuitEditorId, circuitSimulationId);
    this.subcircuitInstanceId = subcircuitInstanceId;
  }
}

