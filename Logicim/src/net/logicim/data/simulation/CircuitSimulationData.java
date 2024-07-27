package net.logicim.data.simulation;

import net.logicim.data.circuit.TimelineData;
import net.logicim.data.common.ReflectiveData;

public class CircuitSimulationData
    extends ReflectiveData
{
  public long id;
  public TimelineData timeline;
  public String name;

  public CircuitSimulationData()
  {
  }

  public CircuitSimulationData(long id, TimelineData timeline, String name)
  {
    this.id = id;
    this.timeline = timeline;
    this.name = name;
  }
}

