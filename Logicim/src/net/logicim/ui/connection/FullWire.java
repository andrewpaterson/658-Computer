package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;

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

  public void process(PortConnection portConnection, List<ComponentViewPortName> portIndexStack)
  {
    if (portConnection == null)
    {
      throw new SimulatorException("PortConnection may not be null.");
    }

    localWires.add(portConnection);
    portIndexStack.addAll(portConnection.getSplitterPortIndices());
  }

  public Set<PortConnection> getLocalWires()
  {
    return localWires;
  }
}

