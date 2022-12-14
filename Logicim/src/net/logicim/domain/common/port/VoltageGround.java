package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class VoltageGround
    extends BasePort
{
  public VoltageGround(Pins pins)
  {
    super(PortType.PowerIn, "GND", pins);
  }

  @Override
  public void reset()
  {
  }

  @Override
  public float getVoltage(long time)
  {
    return 0;
  }
}

