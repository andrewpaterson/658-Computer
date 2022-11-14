package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Omniport;
import net.logicim.domain.common.port.Uniport;

public class OmniportTransitionEvent
    extends OmniportEvent
{
  protected float voltage;
  protected int busIndex;

  public OmniportTransitionEvent(Omniport port, long time, float voltage, int busIndex)
  {
    super(port, time);
    this.voltage = voltage;
    this.busIndex = busIndex;
  }

  @Override
  public void execute(Simulation simulation)
  {
    port.voltageTransition(simulation, voltage, busIndex);
  }
}

