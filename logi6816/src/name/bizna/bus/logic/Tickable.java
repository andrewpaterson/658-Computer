package name.bizna.bus.logic;

import name.bizna.bus.common.Port;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.TraceValue;

import java.util.ArrayList;
import java.util.List;

public abstract class Tickable
{
  protected Tickables tickables;
  protected List<Port> ports;

  public Tickable(Tickables tickables)
  {
    this.tickables = tickables;
    this.tickables.add(this);
    this.ports = new ArrayList<>();
  }

  public void addPort(Port port)
  {
    ports.add(port);
  }

  public abstract void propagate();

  public void startPropagation()
  {
  }

  public void donePropagation()
  {
  }

  public void undoPropagation()
  {
  }

  public List<TraceValue> getTraceValues()
  {
    List<TraceValue> traceValues = new ArrayList<>();
    for (Port port : ports)
    {
      port.addTraceValues(traceValues);
    }
    return traceValues;
  }

  public void updateConnections()
  {
    for (Port port : ports)
    {
      port.updateConnection();
    }
  }

  public void resetConnections()
  {
    for (Port port : ports)
    {
      port.resetConnection();
    }
  }
}

