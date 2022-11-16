package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;

public interface OutputVoltageConfiguration
{
  void createOutputEvents(Timeline timeline, boolean outValue, Port port);

  void createHighImpedanceEvents(Timeline timeline, Port port);

  float getLowVoltageOut();

  float getHighVoltageOut();
}

