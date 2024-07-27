package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;

public class TraceSmoothVoltage
    extends SmoothVoltage
{
  protected Trace trace;

  public TraceSmoothVoltage(Trace trace, float maximumDelta, Simulation simulation)
  {
    super(maximumDelta, simulation);
    this.trace = trace;
  }

  @Override
  protected float getVoltage(long time)
  {
    return trace.getVoltage(time);
  }

  @Override
  protected String getDescription()
  {
    return trace.getDescription();
  }
}

