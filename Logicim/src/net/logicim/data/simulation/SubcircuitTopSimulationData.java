package net.logicim.data.simulation;

public class SubcircuitTopSimulationData
    extends SubcircuitSimulationData
{
  public SubcircuitTopSimulationData()
  {
  }

  public SubcircuitTopSimulationData(long viewPath,
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

