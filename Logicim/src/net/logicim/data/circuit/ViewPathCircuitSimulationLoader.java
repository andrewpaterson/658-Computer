package net.logicim.data.circuit;

import net.common.SimulatorException;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathCircuitSimulationLoader
{
  public Map<Long, Map<Long, ViewPathCircuitSimulation>> viewPathCircuitSimulationMap;

  public ViewPathCircuitSimulationLoader()
  {
    viewPathCircuitSimulationMap = new LinkedHashMap<>();
  }

  public ViewPathCircuitSimulation getViewPathCircuitSimulation(long viewPathId, long circuitSimulationId)
  {
    Map<Long, ViewPathCircuitSimulation> circuitSimulationMap = viewPathCircuitSimulationMap.get(viewPathId);
    if (circuitSimulationMap != null)
    {
      return circuitSimulationMap.get(circuitSimulationId);
    }
    return null;
  }

  public void put(ViewPathCircuitSimulation viewPathCircuitSimulation)
  {
    long viewPathId = viewPathCircuitSimulation.getViewPath().getId();
    long circuitSimulationId = viewPathCircuitSimulation.getCircuitSimulation().getId();

    Map<Long, ViewPathCircuitSimulation> circuitSimulationMap = viewPathCircuitSimulationMap.get(viewPathId);
    if (circuitSimulationMap == null)
    {
      circuitSimulationMap = new LinkedHashMap<>();
      viewPathCircuitSimulationMap.put(viewPathId, circuitSimulationMap);
    }

    ViewPathCircuitSimulation existingViewPathCircuitSimulation = circuitSimulationMap.get(circuitSimulationId);
    if (existingViewPathCircuitSimulation == null)
    {
      circuitSimulationMap.put(circuitSimulationId, viewPathCircuitSimulation);
    }
    else
    {
      if (!existingViewPathCircuitSimulation.equals(viewPathCircuitSimulation))
      {
        throw new SimulatorException("A View Path Circuit Simulation already exists.");
      }
    }
  }
}

