package net.logicim.data.simulation;

public class SubcircuitInstanceSimulationData
    extends SubcircuitSimulationData
{
  public SubcircuitInstanceSimulationData()
  {
  }

  public SubcircuitInstanceSimulationData(long subcircuitSimulationId,
                                          long subcircuitEditorId,
                                          long circuitSimulationId)
  {
    super(subcircuitSimulationId,
          subcircuitEditorId,
          circuitSimulationId);
  }
}

