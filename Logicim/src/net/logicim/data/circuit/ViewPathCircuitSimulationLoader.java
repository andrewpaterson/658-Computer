package net.logicim.data.circuit;

import net.common.SimulatorException;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathCircuitSimulationLoader
{
  Map<Long, Map<Long, ViewPathCircuitSimulation>> viewPathMap;

  public ViewPathCircuitSimulationLoader()
  {
    viewPathMap = new LinkedHashMap<>();
  }

  public ViewPathCircuitSimulation getViewPathCircuitSimulation(long viewPathId, long circuitSimulationId)
  {
    Map<Long, ViewPathCircuitSimulation> circuitSimulationMap = viewPathMap.get(viewPathId);
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

    Map<Long, ViewPathCircuitSimulation> circuitSimulationMap = viewPathMap.get(viewPathId);
    if (circuitSimulationMap == null)
    {
      circuitSimulationMap = new LinkedHashMap<>();
      viewPathMap.put(viewPathId, circuitSimulationMap);
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
        throw new SimulatorException("A View path circuit simulation already exists.");
      }
    }
  }
}

