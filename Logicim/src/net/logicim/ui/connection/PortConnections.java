package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.ui.common.port.PortView;

import java.util.ArrayList;
import java.util.List;

public class PortConnections
{
  public List<Port> connectedPorts;

  public PortConnections()
  {
    this.connectedPorts = new ArrayList<>();
  }

  public void add(Port port)
  {
    if (port != null)
    {
      connectedPorts.add(port);
    }
  }

  public List<Port> getConnectedPorts()
  {
    return connectedPorts;
  }
}

