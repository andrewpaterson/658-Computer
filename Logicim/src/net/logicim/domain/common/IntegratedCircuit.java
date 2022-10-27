package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.common.port.Port;

import java.util.List;

public abstract class IntegratedCircuit<PINS extends Pins>
{
  protected PINS pins;
  protected String name;

  public IntegratedCircuit(String name, PINS pins)
  {
    this.name = name;
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
  }

  public PINS getPins()
  {
    return pins;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    if (StringUtil.isEmptyOrNull(name))
    {
      return getType();
    }
    else
    {
      return getType() + " \"" + name + "\"";
    }
  }

  public Timeline getTimeline()
  {
    return pins.timeline;
  }

  public abstract void tick(long time, List<Port> updatedPorts);

  public abstract String getType();
}

