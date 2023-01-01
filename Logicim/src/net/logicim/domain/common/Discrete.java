package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;

public interface Discrete
{
  PortHolder getPortHolder();

  void disconnect(Simulation simulation);

  default void disconnectPorts(Simulation simulation)
  {
    for (Port port : getPortHolder().getPorts())
    {
      port.disconnect(simulation);
    }
  }
}
