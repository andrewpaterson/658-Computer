package net.logicim.data.simulation;

public class SubcircuitTopSimulationData
    extends SubcircuitSimulationData
{
  public long subcircuitEditorId;

  public SubcircuitTopSimulationData()
  {
  }

  public SubcircuitTopSimulationData(long circuitSimulationId,
                                     long subcircuitEditorId)
  {
    super(circuitSimulationId);
    this.subcircuitEditorId = subcircuitEditorId;
  }
}

