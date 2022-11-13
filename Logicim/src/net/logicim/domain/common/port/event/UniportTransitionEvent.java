package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Uniport;

public class UniportTransitionEvent
    extends UniportEvent
{
  protected float voltage;

  public UniportTransitionEvent(Uniport port, long time, float voltage)
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

