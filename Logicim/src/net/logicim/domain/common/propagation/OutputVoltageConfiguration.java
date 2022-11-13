package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Omniport;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.trace.TraceValue;

public interface OutputVoltageConfiguration
{
  void createOutputEvents(Timeline timeline, TraceValue outValue, Uniport uniport);

  void createOutputEvents(Timeline timeline, TraceValue[] outValues, Omniport omniport);
}

