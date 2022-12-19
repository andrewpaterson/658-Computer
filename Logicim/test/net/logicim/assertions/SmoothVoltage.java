package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.voltage.Voltage;

public abstract class SmoothVoltage
{
  protected float maximumDelta;
  protected Simulation simulation;
  protected float lastValue;

  public SmoothVoltage(float maximumDelta, Simulation simulation)
  {
    this.maximumDelta = maximumDelta;
    this.simulation = simulation;
    this.lastValue = getVoltage(simulation.getTime());
  }

  public void validate()
  {
    float voltage = getVoltage(simulation.getTime());
    if (!Float.isNaN(lastValue) && !Float.isNaN(voltage))
    {
      float delta = Math.abs(voltage - lastValue);
      if (delta > maximumDelta)
      {
        throw new ValidationException("[" + getDescription() + "] voltage changed by [" + Voltage.toVoltageString(delta) + "] from [" + Voltage.toVoltageString(lastValue) + "] to [" + Voltage.toVoltageString(voltage) + "].");
      }
    }
    this.lastValue = voltage;
  }

  protected abstract float getVoltage(long time);

  protected abstract String getDescription();
}

