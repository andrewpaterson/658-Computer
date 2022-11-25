package net.logicim.data;

public class TimelineData
    extends SaveData
{
  public long time;
  public long previousEventTime;
  public long eventTime;

  public TimelineData(long time, long previousEventTime, long eventTime)
  {
    this.time = time;
    this.previousEventTime = previousEventTime;
    this.eventTime = eventTime;
  }
}

