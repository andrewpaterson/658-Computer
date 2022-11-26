package net.logicim.data.circuit;

import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.trace.TraceData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public TimelineData timeline;
  public List<IntegratedCircuitData<?>> integratedCircuits;
  public List<TraceData> traces;

  public CircuitData()
  {
  }

  public CircuitData(TimelineData timeline,
                     List<IntegratedCircuitData<?>> integratedCircuits,
                     List<TraceData> traces)
  {
    this.timeline = timeline;
    this.integratedCircuits = integratedCircuits;
    this.traces = traces;
  }

  public TimelineData getTimeline()
  {
    return timeline;
  }

  public List<IntegratedCircuitData<?>> getIntegratedCircuits()
  {
    return integratedCircuits;
  }

  public List<TraceData> getTraces()
  {
    return traces;
  }

  public void setTimeline(TimelineData timeline)
  {
    this.timeline = timeline;
  }

  public void setIntegratedCircuits(List<IntegratedCircuitData<?>> integratedCircuits)
  {
    this.integratedCircuits = integratedCircuits;
  }

  public void setTraces(List<TraceData> traces)
  {
    this.traces = traces;
  }
}

