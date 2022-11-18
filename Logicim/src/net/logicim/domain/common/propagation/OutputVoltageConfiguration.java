package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.SlewEvent;

public interface OutputVoltageConfiguration
{
  SlewEvent createOutputEvent(Timeline timeline, Port port, float outVoltage);

  void createHighImpedanceEvents(Timeline timeline, Port port);

  float getLowVoltageOut();

  float getMidVoltageOut();

  float getHighVoltageOut();

  float getVoltsPerTimeLowToHigh();

  float getVoltsPerTimeHighToLow();

  float calculateStartVoltage(float portVoltage);

  default float getVoltage(boolean value)
  {
    if (value)
    {
      return getHighVoltageOut();
    }
    else
    {
      return getLowVoltageOut();
    }
  }
}

