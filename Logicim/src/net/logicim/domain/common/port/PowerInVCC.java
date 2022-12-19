package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class PowerInVCC
    extends BasePort
{
  public PowerInVCC(Pins pins)
  {
    super(PortType.PowerIn, "VCC", pins);
  }

  @Override
  public void reset()
  {
  }

  public float getVoltageIn()
  {
    return 3.3f;
  }
}

