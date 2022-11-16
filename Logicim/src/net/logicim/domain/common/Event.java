package net.logicim.domain.common;

import net.logicim.domain.Simulation;

public abstract class Event
{
  protected long time;

  public Event(long time)
  {
    this.time = time;
  }

  public long getTime()
  {
    return time;
  }

  public abstract void execute(Simulation simulation);

  public abstract IntegratedCircuit<?, ?> getIntegratedCircuit();
}

