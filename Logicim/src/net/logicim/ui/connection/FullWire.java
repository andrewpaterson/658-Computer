package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.port.Port;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FullWire
{
  protected Set<PortConnection> localWires;
  protected List<Port> portStack;
  protected int portStackIndex;

  public FullWire()
  {
    localWires = new HashSet<>();
    portStack = new ArrayList<>();
    portStackIndex = 0;
  }

  public void process(PortConnection portConnection)
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

  public boolean hasPortToProcess()
  {
    return portStackIndex < portStack.size();
  }

  public Port getNextPort()
  {
    if (portStackIndex < portStack.size())
    {
      Port port = portStack.get(portStackIndex);
      portStackIndex++;
      return port;
    }
    return null;
  }

  public void done()
  {
    portStack = null;
  }

  public Set<PortConnection> getLocalWires()
  {
    return localWires;
  }
}

