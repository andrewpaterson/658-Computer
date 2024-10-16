package net.logicim.domain.passive.common;

import net.common.SimulatorException;
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
  public String getName()
  {
    return name;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public Port getPort(String name)
  {
    for (Port port : getPorts())
    {
      if (port.getName().equals(name))
      {
        return port;
      }
    }
    throw new SimulatorException("Cannot get port named [%s] on %s [%s].", name, getClass().getSimpleName(), getName());
  }

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  public void reset()
  {
    for (Port port : getPorts())
    {
      port.reset();
    }
  }

  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public Component getComponent()
  {
    return this;
  }

  public void reset(Simulation simulation)
  {
    reset();
    simulationStarted(simulation);
  }

  public abstract String getType();
}

