package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.ui.common.integratedcircuit.DiscreteProperties;

public class PositivePowerProperties
    extends DiscreteProperties
{
  public float voltage_V;

  public PositivePowerProperties()
  {
    voltage_V = 0;
  }

  public PositivePowerProperties(String name, float voltage_V)
  {
    super(name);
    this.voltage_V = voltage_V;
  }
}

