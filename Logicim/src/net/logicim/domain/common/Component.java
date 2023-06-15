package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

import java.util.List;

public interface Component
{
  List<Port> getPorts();

  void disconnect(Simulation simulation);

  void reset(InstanceCircuitSimulation circuit);

  String getType();

  String getName();

  Port getPort(String portName);

  void simulationStarted(Simulation simulation);

  default String getDescription()
  {
    String name = getName();
    if (StringUtil.isEmptyOrNull(name))
    {
      return getType();
    }
    else
    {
      return getType() + " \"" + name + "\"";
    }
  }
}

