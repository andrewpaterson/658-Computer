package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.ui.common.integratedcircuit.DiscreteProperties;

public class PositivePowerProperties
    extends DiscreteProperties
{
  public float voltage;

  public PositivePowerProperties()
  {
    voltage = 0;
  }

  public PositivePowerProperties(String name, float voltage)
  {
    super(name);
    this.voltage = voltage;
  }
}
