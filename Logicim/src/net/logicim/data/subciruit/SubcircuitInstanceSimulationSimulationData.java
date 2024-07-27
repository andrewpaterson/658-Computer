package net.logicim.data.subciruit;

import net.logicim.data.common.ReflectiveData;

public class SubcircuitInstanceSimulationSimulationData
    extends ReflectiveData
{
  public long containingSimulation;
  public long instanceSimulation;

  public SubcircuitInstanceSimulationSimulationData()
  {
  }

  public SubcircuitInstanceSimulationSimulationData(long containingSimulation, long instanceSimulation)
  {
    this.containingSimulation = containingSimulation;
    this.instanceSimulation = instanceSimulation;
  }
}

