package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  protected List<Port> ports;

  public Pins()
  {
    this.ports = new ArrayList<>();
  }

  public void addPort(Port port)
  {
    ports.add(port);
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

  public String getName()
  {
    return integratedCircuit.getName();
  }

  public String getDescription()
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

  public void setIntegratedCircuit(IntegratedCircuit<?, ?> integratedCircuit)
  {
    this.integratedCircuit = integratedCircuit;
  }

  public IntegratedCircuit<? extends Pins, ? extends State> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  public String getType()
  {
    return integratedCircuit.getType();
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public void inputTransition(Simulation simulation, Port port)
  {
    getIntegratedCircuit().inputTransition(simulation, port);
  }
}

