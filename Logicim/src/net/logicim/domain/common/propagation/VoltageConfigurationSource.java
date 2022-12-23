package net.logicim.domain.common.propagation;

import net.logicim.domain.common.trace.TraceValue;

public abstract class VoltageConfigurationSource
{
  public abstract VoltageConfiguration get(float vcc);

  public abstract float getLowVoltageIn(float vcc);

  public abstract float getHighVoltageIn(float vcc);

  public abstract float getVoltageOut(boolean value, float vcc);

  public abstract TraceValue getValue(float voltage, float vcc);

  public abstract float calculateStartVoltage(float portVoltage, float vcc);

  public abstract float getVoltsPerTimeLowToHigh(float vcc);

  public abstract float getVoltsPerTimeHighToLow(float vcc);

  public abstract long calculateHoldTime(float outVoltage, float portVoltage, float vcc);
}

