package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Omniport;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;

public interface OutputVoltageConfiguration
{
  void createDriveEvents(Timeline timeline, boolean outValue, Uniport uniport);

  void createDriveEvents(Timeline timeline, boolean[] outValues, Omniport omniport);

  void createHighImpedanceEvents(Timeline timeline, Port port);
}

