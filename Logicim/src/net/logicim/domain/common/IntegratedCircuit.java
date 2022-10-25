package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;

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

  public void startTick()
  {
  }

  public abstract void tick();

  public void doneTick()
  {
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

  public abstract String getType();
}

