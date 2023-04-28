package net.logicim.domain.passive.common;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;

import java.util.List;

public abstract class Passive
    extends PortHolder
    implements Component
{
  protected Circuit circuit;
  protected String name;

  public Passive(Circuit circuit, String name)
  {
    this.circuit = circuit;
    this.name = name;
    circuit.add(this);
  }

  @Override
  public String getDescription()
  {
    return getType() + " [" + getName() + "]";
  }

  @Override
  public String getName()
  {
    return name;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public abstract String getType();

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

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  public void reset(CircuitSimulation simulation)
  {
    for (Port port : getPorts())
    {
      port.reset();
    }
  }
}

