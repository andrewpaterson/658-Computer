package net.logicim.data.circuit;

import net.logicim.data.ReflectiveData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public TimelineData timeline;
  public List<SubcircuitData> subcircuit;

  public CircuitData()
  {
  }

  public CircuitData(TimelineData timeline, List<SubcircuitData> subcircuit)
  {
    this.timeline = timeline;
    this.subcircuit = subcircuit;
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

