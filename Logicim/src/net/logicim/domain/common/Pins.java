package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.BasePort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.VoltageCommon;
import net.logicim.domain.common.port.VoltageGround;
import net.logicim.domain.common.state.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  protected VoltageCommon vcc;
  protected VoltageGround vss;
  protected List<BasePort> ports;

  public Pins()
  {
    this.vcc = addPort(new VoltageCommon(this));
    this.vss = addPort(new VoltageGround(this));
    this.ports = new ArrayList<>();
  }

  public <T extends BasePort> T addPort(T port)
  {
    ports.add(port);
    return port;
  }

  public BasePort getPort(String name)
  {
    for (BasePort port : ports)
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

  public List<BasePort> getPorts()
  {
    return ports;
  }

  public void inputTransition(Simulation simulation, Port port)
  {
    getIntegratedCircuit().inputTransition(simulation, port);
  }

  public VoltageCommon getVoltageCommon()
  {
    return vcc;
  }

  public VoltageGround getVoltageGround()
  {
    return vss;
  }
}

