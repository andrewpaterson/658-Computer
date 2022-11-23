package net.logicim.data;

import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.trace.TraceData;

import java.util.List;

public class CircuitData
{
  protected TimelineData timelineData;
  protected List<IntegratedCircuitData> integratedCircuits;
  protected List<TraceData> traces;

  public CircuitData(TimelineData timelineData,
                     List<IntegratedCircuitData> integratedCircuits,
                     List<TraceData> traces)
  {
    this.timelineData = timelineData;
    this.integratedCircuits = integratedCircuits;
    this.traces = traces;
  }
}

