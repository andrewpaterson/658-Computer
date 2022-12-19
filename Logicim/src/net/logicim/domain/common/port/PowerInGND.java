package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class PowerInGND
    extends BasePort
{
  public PowerInGND(Pins pins)
  {
    super(PortType.PowerIn, "GND", pins);
  }

  @Override
  public void reset()
  {
  }

  public float getVoltageIn()
  {
    return 0;
  }
}

