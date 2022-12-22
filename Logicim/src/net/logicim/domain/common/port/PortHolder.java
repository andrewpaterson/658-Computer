package net.logicim.domain.common.port;

import net.logicim.domain.Simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class PortHolder
{
  protected List<Port> ports;

  public PortHolder()
  {
    this.ports = new ArrayList<>();
  }

  public <T extends Port> T addPort(T port)
  {
    ports.add(port);
    return port;
  }

  public Port getPort(String name)
  {
    for (Port port : ports)
    {
      if (port.getName().equals(name))
      {
        return port;
      }
    }
    return null;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public abstract void traceConnected(Simulation simulation, Port port);

  public abstract String getDescription();

  public abstract String getName();
}

