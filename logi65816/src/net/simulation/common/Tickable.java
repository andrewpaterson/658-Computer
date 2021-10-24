package net.simulation.common;

import net.simulation.common.Port;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.TraceValue;
import net.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

  public abstract void startPropagation();

  public abstract void propagate();

  public abstract void donePropagation();

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
        stringBuilder.append(portTransmissionState).append(": P&W").append(portValues);
      }
      else
      {
        stringBuilder.append(portTransmissionState).append(":   P").append(portValues).append(" W");
        stringBuilder.append(connectionValues);
      }
      List<Trace> connections = port.getConnections();
      Set<Port> updatingPorts = get_DEBUG_UpdatingPorts(connections);
      if (updatingPorts.size() > 1)
      {
        stringBuilder.append(" (Multiple updaters)");
      }
      else if (updatingPorts.size() == 1)
      {
        stringBuilder.append(" (").append(updatingPorts.iterator().next().getDescription()).append(")");
      }

      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }

  private Set<Port> get_DEBUG_UpdatingPorts(List<Trace> connections)
  {
    Set<Port> updatingPorts = new LinkedHashSet<>();
    for (Trace connection : connections)
    {
      Port updatingPort = connection.get_DEBUG_lastPortThatUpdated();
      if (updatingPort != null)
      {
        updatingPorts.add(updatingPort);
      }
    }
    return updatingPorts;
  }

  public abstract String getType();
}

