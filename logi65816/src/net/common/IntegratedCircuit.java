package net.common;

import net.util.StringUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class IntegratedCircuit<SNAPSHOT extends Snapshot, PINS extends Pins>
{
  private final PINS pins;
  private final String name;

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

  public SNAPSHOT createSnapshot()
  {
    return null;
  }

  public void restoreFromSnapshot(SNAPSHOT snapshot)
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

  public String getPart()
  {
    return getClass().getSimpleName();
  }

  public abstract String getType();
}

