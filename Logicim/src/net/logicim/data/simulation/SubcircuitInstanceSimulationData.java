package net.logicim.data.simulation;

public class SubcircuitInstanceSimulationData
    extends SubcircuitSimulationData
{
  public long subcircuitInstanceId;

  public SubcircuitInstanceSimulationData()
  {
  }

  public SubcircuitInstanceSimulationData(long circuitSimulationId,
                                          long subcircuitInstanceId)
  {
    super(circuitSimulationId);
    this.subcircuitInstanceId = subcircuitInstanceId;
  }
}

