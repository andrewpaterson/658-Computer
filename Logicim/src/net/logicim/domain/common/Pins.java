package net.logicim.domain.common;

import net.common.util.StringUtil;
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
    vcc = new PowerInPort(PortType.PowerIn, "VCC", this);
    vss = new PowerInPort(PortType.PowerIn, "GND", this);
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
    getIntegratedCircuit().inputTransition(simulation.getTimeline(), port);
  }

  public PowerInPort getVoltageCommon()
  {
    return vcc;
  }

  public PowerInPort getVoltageGround()
  {
    return vss;
  }

  protected String calculatePortName(String name, int i, int bufferCount)
  {
    if (bufferCount > 1)
    {
      return name + " " + i;
    }
    else
    {
      return name;
    }
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
    integratedCircuit.traceConnected(simulation, port);
  }

  @Override
  public Component getComponent()
  {
    return integratedCircuit;
  }
}

