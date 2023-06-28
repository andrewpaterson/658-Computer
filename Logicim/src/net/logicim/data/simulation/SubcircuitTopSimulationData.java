package net.logicim.data.simulation;

public class SubcircuitTopSimulationData
    extends SubcircuitSimulationData
{
  public SubcircuitTopSimulationData()
  {
  }

  public SubcircuitTopSimulationData(long subcircuitSimulationId,
                                     long subcircuitEditorId,
                                     long circuitSimulationId)
  {
    super(subcircuitSimulationId,
          subcircuitEditorId,
          circuitSimulationId);
    this.subcircuitEditorId = subcircuitEditorId;
  }
}

