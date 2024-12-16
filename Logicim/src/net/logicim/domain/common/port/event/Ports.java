package net.logicim.domain.common.port.event;

import net.logicim.domain.common.CircuitElement;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class Ports
    implements CircuitElement
{
  protected List<Port> ports;
  protected SubcircuitSimulation containingSubcircuitSimulation;

  public Ports(List<Port> ports, SubcircuitSimulation containingSubcircuitSimulation)
  {
    this.ports = ports;
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
  }

  public int size()
  {
    return ports.size();
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public void disconnect()
  {
    for (Port port : ports)
    {
      port.disconnect(containingSubcircuitSimulation.getSimulation());
    }
  }

  @Override
  public String getDescription()
  {
    return "Ports";
  }

  @Override
  public String getType()
  {
    return "Ports";
  }

  @Override
  public SubcircuitSimulation getContainingSubcircuitSimulation()
  {
    return containingSubcircuitSimulation;
  }
}

