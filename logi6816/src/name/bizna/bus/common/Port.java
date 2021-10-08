package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;

import java.util.List;

public abstract class Port
{
  protected Tickable tickable;

  public Port(Tickable tickable)
  {
    this.tickable = tickable;
    tickable.addPort(this);
  }

  public abstract void startPropagation();

  public abstract void addTraceValues(List<TraceValue> traceValues);

  public abstract void updateConnection();

  public abstract void resetConnection();
}

