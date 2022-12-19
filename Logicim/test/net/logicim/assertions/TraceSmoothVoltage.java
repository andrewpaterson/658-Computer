package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.trace.TraceNet;

public class TraceSmoothVoltage
    extends SmoothVoltage
{
  protected TraceNet traceNet;

  public TraceSmoothVoltage(TraceNet traceNet, float maximumDelta, Simulation simulation)
  {
    super(maximumDelta, simulation);
    this.traceNet = traceNet;
  }

  @Override
  protected float getVoltage(long time)
  {
    return traceNet.getVoltage(time);
  }

  @Override
  protected String getDescription()
  {
    return traceNet.getDescription();
  }
}

