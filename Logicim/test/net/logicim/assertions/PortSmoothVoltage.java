package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.LogicPort;

public class PortSmoothVoltage
    extends SmoothVoltage
{
  protected LogicPort port;

  public PortSmoothVoltage(LogicPort port, float maximumDelta, Simulation simulation)
  {
    super(maximumDelta, simulation);
    this.port = port;
  }

  @Override
  protected float getVoltage(long time)
  {
    return port.getVoltageOut(time);
  }

  @Override
  protected String getDescription()
  {
    return port.getDescription();
  }
}

