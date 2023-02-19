package net.logicim.domain.common;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

import java.util.List;

public interface Component
{
  List<Port> getPorts();

  void disconnect(Simulation simulation);

  boolean isEnabled();

  void enable(Simulation simulation);

  void disable();

  String getType();
}

