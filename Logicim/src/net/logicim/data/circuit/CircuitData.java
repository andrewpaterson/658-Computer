package net.logicim.data.circuit;

import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public TimelineData timeline;
  public List<StaticData<?>> components;
  public List<TraceData> traces;

  public CircuitData()
  {
  }

  public CircuitData(TimelineData timeline,
                     List<StaticData<?>> components,
                     List<TraceData> traces)
  {
    this.timeline = timeline;
    this.components = components;
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

