package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceValue;

public abstract class VoltageConfigurationSource
{
  public abstract VoltageConfiguration get(float vcc);

  public abstract float getLowVoltageIn(float vcc);

  public abstract float getHighVoltageIn(float vcc);

  public abstract float getMidVoltageOut(float vcc);

  public abstract void createOutputEvent(Timeline timeline, Port port, float voltageOut);

  public abstract float getVoltageOut(boolean value, float vcc);

  public abstract TraceValue getValue(float voltage, float vcc);

  public abstract float calculateStartVoltage(float portVoltage, float vcc);

  public abstract float getVoltsPerTimeLowToHigh(float vcc);

  public abstract float getVoltsPerTimeHighToLow(float vcc);
}

