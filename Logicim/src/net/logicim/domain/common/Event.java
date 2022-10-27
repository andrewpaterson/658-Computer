package net.logicim.domain.common;

import net.logicim.domain.common.trace.TraceNet;

public class Event
{
  protected long time;
  protected float voltage;
  protected TraceNet trace;

  public Event(long time, float voltage, TraceNet trace)
  {
    this.time = time;
    this.voltage = voltage;
    this.trace = trace;
  }

  public long getTime()
  {
    return time;
  }

  public float getVoltage()
  {
    return voltage;
  }

  public TraceNet getTrace()
  {
    return trace;
  }
}

