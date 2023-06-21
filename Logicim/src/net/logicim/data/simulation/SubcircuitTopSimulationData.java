package net.logicim.data.simulation;

public class SubcircuitTopSimulationData
    extends SubcircuitSimulationData
{
  public SubcircuitTopSimulationData()
  {
  }

  public SubcircuitTopSimulationData(long subcircuitSimulationId,
                                     long circuitSimulationId,
                                     long subcircuitEditorId)
  {
    super(subcircuitSimulationId, subcircuitEditorId, circuitSimulationId);
  }
}

