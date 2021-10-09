package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;

import java.util.List;

public abstract class Port
{
  protected Tickable tickable;
  protected String name;

  public Port(Tickable tickable,  String name)
  {
    this.tickable = tickable;
    this.name = name;
    tickable.addPort(this);
  }

  public abstract void startPropagation();

  public abstract void addTraceValues(List<TraceValue> traceValues);

  public abstract void updateConnection();

  public abstract void resetConnection();
}

