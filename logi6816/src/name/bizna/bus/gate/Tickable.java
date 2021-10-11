package name.bizna.bus.gate;

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

  @Override
  public String toString()
  {
    return getDescription();
  }

  public String toDebugString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    String description = " " + getDescription() + " ";
    stringBuilder.append(" ").append(StringUtil.centerJustify(description, 48, "-")).append(" \n");
    for (Port port : ports)
    {
      String portTransmissionState = StringUtil.rightJustify(port.getPortTransmissionStateAsString(), 24, " ");
      String portValues = port.getTraceValuesAsString();
      String connectionValues = port.getConnectionValuesAsString();
      if (portValues.equals(connectionValues))
      {
        stringBuilder.append(portTransmissionState).append(": P&C").append(portValues);
      }
      else
      {
        stringBuilder.append(portTransmissionState).append(":   P").append(portValues).append(" C");
        stringBuilder.append(connectionValues);
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }

  public abstract String getType();
}

