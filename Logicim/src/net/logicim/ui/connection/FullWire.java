package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.port.Port;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullWire
{
  protected Set<PortConnection> localWires;

  public FullWire()
  {
    localWires = new HashSet<>();
  }

  public void process(PortConnection portConnection, List<Port> portStack)
  {
    if (portConnection == null)
    {
      throw new SimulatorException("PortConnection may not be null.");
    }

    localWires.add(portConnection);
    Set<Port> splitterPorts = portConnection.getSplitterPorts();
    for (Port splitterPort : splitterPorts)
    {
      portStack.add(splitterPort);
    }
  }

  public Set<PortConnection> getLocalWires()
  {
    return localWires;
  }
}

