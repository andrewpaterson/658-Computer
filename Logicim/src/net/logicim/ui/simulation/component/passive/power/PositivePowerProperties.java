package net.logicim.ui.simulation.component.passive.power;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class PositivePowerProperties
    extends ComponentProperties
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

