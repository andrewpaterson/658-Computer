package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.voltage.Voltage;

public class PowerOutPort
    extends Port
{
  protected float voltage;

  public PowerOutPort(PortType type, String name, PortHolder holder, float voltage)
  {
    super(type, name, holder);
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

  @Override
  public boolean isPowerOut()
  {
    return true;
  }

  public float getVoltageOut()
  {
    return voltage;
  }
}
