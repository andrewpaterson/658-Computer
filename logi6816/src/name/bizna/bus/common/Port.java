package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;

import java.util.List;

import static name.bizna.bus.common.TransmissionState.NotSet;

public abstract class Port
{
  protected Tickable tickable;
  protected String name;
  protected TransmissionState state;

  public Port(Tickable tickable, String name)
  {
    this.tickable = tickable;
    this.name = name;
    this.state = NotSet;
    tickable.addPort(this);
  }

  public abstract void resetConnections();

  public abstract void addTraceValues(List<TraceValue> traceValues);

  public abstract void updateConnection();
}

