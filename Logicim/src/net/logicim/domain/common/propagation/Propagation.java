package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceValue;

public abstract class Propagation
{
  protected Timeline timeline;
  protected String family;

  public Propagation(Timeline timeline, String family)
  {
    this.timeline = timeline;
    this.family = family;
  }

  public Timeline getTimeline()
  {
    return timeline;
  }

  public TraceValue getValue(boolean value)
  {
    return value ? TraceValue.High : TraceValue.Low;
  }

  public boolean isInput()
  {
    return false;
  }

  public  boolean isOutput()
  {
    return false;
  }
}

