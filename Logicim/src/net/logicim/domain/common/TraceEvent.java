package net.logicim.domain.common;

import net.logicim.domain.common.trace.TraceNet;

public class TraceEvent
    extends Event
{
  protected float voltage;
  protected TraceNet trace;

  public TraceEvent(long time, float voltage, TraceNet trace)
  {
    super(time);
    this.voltage = voltage;
    this.trace = trace;
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

