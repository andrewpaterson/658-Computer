package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class VoltageCommon
    extends BasePort
{
  public VoltageCommon(Pins pins)
  {
    super(PortType.PowerIn, "VCC", pins);
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

