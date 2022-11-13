package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Uniport;

public class UniportDriveEvent
    extends UniportEvent
{
  protected UniportSlewEvent slewEvent;

  public UniportDriveEvent(Uniport port, UniportSlewEvent slewEvent)
  {
    super(port, slewEvent.getEndTime());
    this.slewEvent = slewEvent;
  }

  @Override
  public void execute(Simulation simulation)
  {
    port.voltageDriven(simulation, slewEvent.getEndVoltage());
  }
}

