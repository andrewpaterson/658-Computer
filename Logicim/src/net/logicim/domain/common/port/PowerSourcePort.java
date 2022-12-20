package net.logicim.domain.common.port;

import net.logicim.domain.common.voltage.Voltage;

public class PowerSourcePort
    extends BasePort
{
  protected float voltage;

  public PowerSourcePort(PortType type, String name, float voltage)
  {
    super(type, name);
    this.voltage = voltage;
  }

  @Override
  public String toDebugString()
  {
    return "Power" + "." + getDescription();
  }

  @Override
  public String getDescription()
  {
    return name + " (" + Voltage.toVoltageString(voltage) + ")";
  }

  @Override
  public void reset()
  {
  }

  public float getVoltageOut()
  {
    return voltage;
  }
}

