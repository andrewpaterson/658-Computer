package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;

public class PowerInPort
    extends Port
{
  protected Pins pins;

  public PowerInPort(PortType portType, String name, Pins pins)
  {
    super(portType, name);
    this.pins = pins;
  }

  @Override
  public String toDebugString()
  {
    return getPins().getIntegratedCircuit().getName() + "." + name;
  }

  @Override
  public String getDescription()
  {
    return getPins().getDescription() + "." + getName();
  }

  @Override
  public void reset()
  {
  }

  @Override
  public boolean isPowerIn()
  {
    return true;
  }

  public float getVoltageIn()
  {
    return 3.3f;
  }

  public Pins getPins()
  {
    return pins;
  }
}

