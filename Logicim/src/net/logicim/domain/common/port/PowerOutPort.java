package net.logicim.domain.common.port;

import net.logicim.domain.common.voltage.Voltage;

public class PowerOutPort
    extends Port
{
  protected float voltage;

  public PowerOutPort(String name, PortHolder holder, float voltage)
  {
    super(PortType.PowerOut, name, holder);
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

  public float getVoltageOut(long time)
  {
    return voltage;
  }
}

