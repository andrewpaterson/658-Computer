package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.common.port.Port;

import java.util.ArrayList;
import java.util.List;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins> integratedCircuit;

  protected List<Port> ports;
  protected Timeline timeline;

  public Pins(Timeline timeline)
  {
    this.timeline = timeline;
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

  public IntegratedCircuit<? extends Pins> getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  public String getType()
  {
    return integratedCircuit.getType();
  }
}

