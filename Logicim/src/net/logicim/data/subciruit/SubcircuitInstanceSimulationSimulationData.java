package net.logicim.data.subciruit;

import net.logicim.data.common.ReflectiveData;

public class SubcircuitInstanceSimulationSimulationData
    extends ReflectiveData
{
  public long containingSubcircuitSimulation;
  public long circuitSimulation;
  public long instanceSubcircuitSSimulation;
  public long viewPath;

  public SubcircuitInstanceSimulationSimulationData()
  {
  }

  public SubcircuitInstanceSimulationSimulationData(long containingSubcircuitSimulation,
                                                    long instanceSubcircuitSSimulation,
                                                    long viewPath,
                                                    long circuitSimulation)
  {
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.instanceSubcircuitSSimulation = instanceSubcircuitSSimulation;
    this.viewPath = viewPath;
    this.circuitSimulation = circuitSimulation;
  }
}

