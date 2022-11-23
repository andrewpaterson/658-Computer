package net.logicim.data;

public class TimelineData
{
  protected long time;
  protected long previousEventTime;
  protected long eventTime;

  public TimelineData(long time, long previousEventTime, long eventTime)
  {
    this.time = time;
    this.previousEventTime = previousEventTime;
    this.eventTime = eventTime;
  }
}

