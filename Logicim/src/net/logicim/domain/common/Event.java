package net.logicim.domain.common;

public class Event
{
  protected final long time;
  protected final float voltage;
  protected final Trace trace;

  public Event(long time, float voltage, Trace trace)
  {
    this.time = time;
    this.voltage = voltage;
    this.trace = trace;
  }

  public long getTime()
  {
    return time;
  }
}

