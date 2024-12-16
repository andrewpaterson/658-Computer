package net.logicim.domain.common;

import net.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public interface Component
    extends CircuitElement
{
  List<Port> getPorts();

  void disconnect(Simulation simulation);

  void simulationStarted(Simulation simulation);

  void reset(Simulation simulation);

  String getType();

  String getName();

  Port getPort(String portName);

  Circuit getCircuit();

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

