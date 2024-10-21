package net.logicim.ui.connection;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FullWire
{
  protected Set<ComponentViewPortNames> localWires;

  public FullWire()
  {
    localWires = new HashSet<>();
  }

  public void process(ComponentViewPortNames componentViewPortNames, List<ComponentViewPortName> portIndexStack)
  {
    if (componentViewPortNames == null)
    {
      throw new SimulatorException("PortConnection may not be null.");
    }

    localWires.add(componentViewPortNames);
    portIndexStack.addAll(componentViewPortNames.getSplitterPortIndices());
  }

  public Set<ComponentViewPortNames> getLocalWires()
  {
    return localWires;
  }

  public Set<ComponentViewPortNames> getLocalWires(CircuitSimulation circuitSimulation)
  {
    Set<ComponentViewPortNames> result = new LinkedHashSet<>();
    for (ComponentViewPortNames localWire : localWires)
    {
      if (localWire.getMultiSimulationConnectionNet().getPath().containsCircuitSimulation(circuitSimulation))
      {
        result.add(localWire);
      }
    }
    return result;
  }
}

