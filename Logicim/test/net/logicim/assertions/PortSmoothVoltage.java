package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public class PortSmoothVoltage
    extends SmoothVoltage
{
  protected Port port;

  public PortSmoothVoltage(Port port, float maximumDelta, Simulation simulation)
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

