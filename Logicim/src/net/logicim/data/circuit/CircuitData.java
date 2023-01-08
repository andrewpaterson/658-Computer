package net.logicim.data.circuit;

import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.trace.TraceData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public TimelineData timeline;
  public List<IntegratedCircuitData<?, ?>> integratedCircuits;
  public List<PassiveData> passives;
  public List<TraceData> traces;

  public CircuitData()
  {
  }

  public CircuitData(TimelineData timeline,
                     List<IntegratedCircuitData<?, ?>> integratedCircuits,
                     List<PassiveData> passives,
                     List<TraceData> traces)
  {
    this.timeline = timeline;
    this.integratedCircuits = integratedCircuits;
    this.passives = passives;
    this.traces = traces;
  }

  public TimelineData getTimeline()
  {
    return timeline;
  }

  public void setTimeline(TimelineData timeline)
  {
    this.timeline = timeline;
  }
}

