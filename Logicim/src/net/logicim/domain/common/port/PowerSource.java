package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class PowerSource
    extends BasePort
{
  public PowerSource(PortType type, String name, Pins pins)
  {
    super(type, name, pins);
  }

  @Override
  public void reset()
  {
  }

  public float getVoltageOut()
  {
    return 0;
  }
}

