package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins> integratedCircuit;

  protected List<Port> ports;

  public Pins(Timeline timeline)
  {
    timeline.add(this);
    this.ports = new ArrayList<>();
  }

  public void addPort(Port port)
  {
    ports.add(port);
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

  public void setIntegratedCircuit(IntegratedCircuit<? extends Pins> integratedCircuit)
  {
    this.integratedCircuit = integratedCircuit;
  }

  public String getType()
  {
    return integratedCircuit.getType();
  }
}

