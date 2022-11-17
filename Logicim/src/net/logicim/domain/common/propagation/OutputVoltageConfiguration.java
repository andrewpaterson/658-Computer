package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;

public interface OutputVoltageConfiguration
{
  void createOutputEvents(Timeline timeline, Port port, float outVoltage);

  void createHighImpedanceEvents(Timeline timeline, Port port);

  float getLowVoltageOut();

  float getMidVoltageOut();

  float getHighVoltageOut();

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

