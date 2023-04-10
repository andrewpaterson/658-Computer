package net.logicim.data.simulation;

import net.logicim.data.circuit.TimelineData;
import net.logicim.data.common.ReflectiveData;

public class CircuitSimulationData
    extends ReflectiveData
{
  public TimelineData timeline;
  public long circuitSimulationId;
  public long subcircuitId;
  public String circuitSimulationName;

  public CircuitSimulationData()
  {
  }

  public CircuitSimulationData(TimelineData timeline,
                               long circuitSimulationId,
                               String circuitSimulationName,
                               long subcircuitId)
  {
    this.timeline = timeline;
    this.circuitSimulationId = circuitSimulationId;
    this.circuitSimulationName = circuitSimulationName;
    this.subcircuitId = subcircuitId;
  }
}

