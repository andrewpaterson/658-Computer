package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Omniport;

public class OmniportDriveEvent
    extends OmniportEvent
{
  protected OmniportSlewEvent slewEvent;

  public OmniportDriveEvent(Omniport port, OmniportSlewEvent slewEvent, int busIndex)
  {
    super(port, slewEvent.getEndTime(busIndex));
    this.slewEvent = slewEvent;
  }

  @Override
  public void execute(Simulation simulation)
  {
    port.setOutputVoltages(slewEvent.getEndVoltages());
  }
}

