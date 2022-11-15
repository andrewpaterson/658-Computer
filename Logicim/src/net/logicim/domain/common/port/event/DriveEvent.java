package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public class DriveEvent
    extends PortEvent
{
  protected SlewEvent slewEvent;

  public DriveEvent(Port port, SlewEvent slewEvent)
  {
    super(port, slewEvent.getEndTime());
    this.slewEvent = slewEvent;
  }

  @Override
  public void execute(Simulation simulation)
  {
    port.voltageDriven(simulation, slewEvent.getEndVoltage());
  }

  public SlewEvent getSlewEvent()
  {
    return slewEvent;
  }
}

