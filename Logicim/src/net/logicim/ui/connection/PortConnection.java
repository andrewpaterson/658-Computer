package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.*;

public class PortConnection
{
  protected LocalMultiSimulationConnectionNet multiSimulationConnectionNet;

  protected Map<SubcircuitSimulation, List<Port>> connectedPorts;
  protected Set<Port> splitterPorts;

  public PortConnection(LocalMultiSimulationConnectionNet multiSimulationConnectionNet)
  {
    this.multiSimulationConnectionNet = multiSimulationConnectionNet;
    this.connectedPorts = new LinkedHashMap<>();
    this.splitterPorts = new HashSet<>();
  }

  public void addPort(SubcircuitSimulation subcircuitSimulation, Port port)
  {
    List<Port> ports = connectedPorts.get(subcircuitSimulation);
    if (ports == null)
    {
      ports = new ArrayList<>();
      connectedPorts.put(subcircuitSimulation, ports);
    }
    ports.add(port);
  }

  public void addSplitterPort(Port port)
  {
    if (port != null)
    {
      splitterPorts.add(port);
    }
  }

  public Set<Port> getSplitterPorts()
  {
    return splitterPorts;
  }

  public LocalMultiSimulationConnectionNet getMultiSimulationConnectionNet()
  {
    return multiSimulationConnectionNet;
  }

  public Map<SubcircuitSimulation, List<Port>> getConnectedPorts()
  {
    return connectedPorts;
  }
}

