package net.logicim.assertions;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.voltage.Voltage;

public class SmoothVoltage
{
  protected Voltage voltageSource;
  protected float maximumDelta;
  protected Simulation simulation;
  protected float lastValue;

  public SmoothVoltage(Voltage voltageSource, float maximumDelta, Simulation simulation)
  {
    this.voltageSource = voltageSource;
    this.maximumDelta = maximumDelta;
    this.simulation = simulation;
    this.lastValue = voltageSource.getVoltage(simulation.getTime());
  }

  public void validate()
  {
    float voltage = voltageSource.getVoltage(simulation.getTime());
    if (!Float.isNaN(lastValue) && !Float.isNaN(voltage))
    {
      float delta = Math.abs(voltage - lastValue);
      if (delta > maximumDelta)
      {
        throw new ValidationException("[" + voltageSource.getDescription() + "] voltage changed by [" + Voltage.toVoltageString(delta) + "] from [" + Voltage.toVoltageString(lastValue) + "] to [" + Voltage.toVoltageString(voltage) + "].");
      }
    }
    this.lastValue = voltage;
  }
}

