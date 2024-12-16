package net.logicim.data.simulation;

public class SubcircuitInstanceSimulationData
    extends SubcircuitSimulationData
{
  public SubcircuitInstanceSimulationData()
  {
  }

  public SubcircuitInstanceSimulationData(long viewPath,
                                          long subcircuitEditorId,
                                          long circuitSimulationId,
                                          long subcircuitSimulation)
  {
    super(viewPath,
          subcircuitEditorId,
          circuitSimulationId,
          subcircuitSimulation);
  }
}

