package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public class TransitionEvent
    extends PortInputEvent
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
    super.execute(simulation);

    port.transitionEvent(simulation, this);
  }
}
