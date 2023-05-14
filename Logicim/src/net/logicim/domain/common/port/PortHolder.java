package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;

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
    throw new SimulatorException("Cannot get port named [%s] on %s [%s].", name, getClass().getSimpleName(), getName());
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public abstract void traceConnected(Simulation simulation, Port port);

  public abstract String getName();

  public abstract Component getComponent();
}

