package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.state.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  protected PowerInPort vcc;
  protected PowerInPort vss;
  protected List<Port> ports;

  public Pins()
  {
    this.ports = new ArrayList<>();
    this.vcc = addPort(new PowerInPort(PortType.PowerIn, "VCC", this));
    this.vss = addPort(new PowerInPort(PortType.PowerIn, "GND", this));
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

  public void inputTransition(Simulation simulation, LogicPort port)
  {
    getIntegratedCircuit().inputTransition(simulation, port);
  }

  public PowerInPort getVoltageCommon()
  {
    return vcc;
  }

  public PowerInPort getVoltageGround()
  {
    return vss;
  }
}

