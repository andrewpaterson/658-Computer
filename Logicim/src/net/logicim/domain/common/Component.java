package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;

import java.util.List;

public interface Component
{
  PortHolder getPortHolder();

  List<Port> getPorts();

  void disconnect(Simulation simulation);

  default void disconnectPorts(Simulation simulation)
  {
    for (Port port : getPortHolder().getPorts())
    {
      port.disconnect(simulation);
    }
  }

  boolean isEnabled();

  void enable(Simulation simulation);

  void disable();

  String getType();
}

