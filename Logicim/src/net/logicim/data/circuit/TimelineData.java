package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;

public class TimelineData
    extends ReflectiveData
{
  public long time;
  public long previousEventTime;
  public long eventTime;

  public TimelineData()
  {
  }

  public TimelineData(long time, long previousEventTime, long eventTime)
  {
    this.time = time;
    this.previousEventTime = previousEventTime;
    this.eventTime = eventTime;
  }
}

