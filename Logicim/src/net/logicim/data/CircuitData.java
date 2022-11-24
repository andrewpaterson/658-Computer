package net.logicim.data;

import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.trace.TraceData;

import java.util.List;

public class CircuitData
{
  public TimelineData timelineData;
  public List<IntegratedCircuitData> integratedCircuits;
  public List<TraceData> traces;

  public CircuitData(TimelineData timelineData,
                     List<IntegratedCircuitData> integratedCircuits,
                     List<TraceData> traces)
  {
    this.timelineData = timelineData;
    this.integratedCircuits = integratedCircuits;
    this.traces = traces;
  }

  public TimelineData getTimelineData()
  {
    return timelineData;
  }

  public List<IntegratedCircuitData> getIntegratedCircuits()
  {
    return integratedCircuits;
  }

  public List<TraceData> getTraces()
  {
    return traces;
  }
}

