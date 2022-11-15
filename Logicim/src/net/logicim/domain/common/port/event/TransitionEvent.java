package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public class TransitionEvent
    extends PortEvent
{
  protected float voltage;

  public TransitionEvent(Port port, long time, float voltage)
  {
    super(port, time);
    this.voltage = voltage;
  }

  @Override
  public void execute(Simulation simulation)
  {
    port.voltageTransition(simulation, voltage);
  }
}

