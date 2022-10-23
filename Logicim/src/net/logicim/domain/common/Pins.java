package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class Pins
{
  protected IntegratedCircuit<? extends Pins> integratedCircuit;

  protected Timeline timeline;
  protected List<Port> ports;

  public Pins(Timeline timeline)
  {
    this.timeline = timeline;
    this.timeline.add(this);
    this.ports = new ArrayList<>();
  }

  public void addPort(Port port)
  {
    ports.add(port);
  }

  public String getName()
  {
    return integratedCircuit.getName();
  }

  public String getDescription()
  {
    String name = getName();
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

  public void setIntegratedCircuit(IntegratedCircuit<? extends Pins> integratedCircuit)
  {
    this.integratedCircuit = integratedCircuit;
  }

  public String getType()
  {
    return integratedCircuit.getType();
  }

  public Timeline getTimeline()
  {
    return timeline;
  }
}

