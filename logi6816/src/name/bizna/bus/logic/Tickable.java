package name.bizna.bus.logic;

import name.bizna.bus.common.Port;
import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.TraceValue;
import name.bizna.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class Tickable
{
  protected Tickables tickables;
  protected List<Port> ports;
  protected String name;

  public Tickable(Tickables tickables, String name)
  {
    this.tickables = tickables;
    this.name = name;
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
      port.resetConnections();
    }
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

