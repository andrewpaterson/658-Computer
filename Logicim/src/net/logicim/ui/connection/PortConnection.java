package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PortConnection
{
  protected LocalConnectionNet localConnectionNet;

  protected List<Port> connectedPorts;
  protected Set<Port> splitterPorts;

  public PortConnection(LocalConnectionNet localConnectionNet)
  {
    this.localConnectionNet = localConnectionNet;
    this.connectedPorts = new ArrayList<>();
    this.splitterPorts = new HashSet<>();
  }

  public void addPort(Port port)
  {
    if (port != null)
    {
      connectedPorts.add(port);
    }
  }

  public void addSplitterPort(Port port)
  {
    if (port != null)
    {
      splitterPorts.add(port);
    }
  }

  public Set<Port> getSplitterPorts()
  {
    return splitterPorts;
  }
}

