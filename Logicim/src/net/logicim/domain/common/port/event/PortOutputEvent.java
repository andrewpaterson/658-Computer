package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public abstract class PortOutputEvent
    extends PortEvent
{
  public PortOutputEvent(Port port, long time)
  {
    super(port, time);
  }

  public abstract float getVoltage(long time);

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
  }
}

