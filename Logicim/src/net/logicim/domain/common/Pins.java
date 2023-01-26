package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.*;
import net.logicim.domain.common.state.State;

public abstract class Pins
    extends PortHolder
{
  protected IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit;

  protected PowerInPort vcc;
  protected PowerInPort vss;

  public Pins()
  {
    super();
    this.vcc = addPort(new PowerInPort(PortType.PowerIn, "VCC", this));
    this.vss = addPort(new PowerInPort(PortType.PowerIn, "GND", this));
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

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
    integratedCircuit.traceConnected(simulation, port);
  }
}

