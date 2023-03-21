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

  public PositivePowerProperties(String name, float voltage)
  {
    super(name);
    this.voltage_V = voltage;
  }

  @Override
  public PositivePowerProperties duplicate()
  {
    return new PositivePowerProperties(name, voltage_V);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    PositivePowerProperties that = (PositivePowerProperties) o;
    return Float.compare(that.voltage_V, voltage_V) == 0;
  }
}

