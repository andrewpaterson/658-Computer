package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathComponentSimulation<COMPONENT extends Component>
{
  protected Map<ViewPath, Map<CircuitSimulation, COMPONENT>> map;

  public ViewPathComponentSimulation()
  {
    this.map = new LinkedHashMap<>();
  }

  public void put(ViewPath path, CircuitSimulation circuitSimulation, COMPONENT component)
  {
    Map<CircuitSimulation, COMPONENT> simulationMap = map.get(path);
    if (simulationMap == null)
    {
      simulationMap = new LinkedHashMap<>();
      map.put(path, simulationMap);
    }

    COMPONENT existingComponent = simulationMap.get(circuitSimulation);
    if (existingComponent != null)
    {
      throw new SimulatorException("A Component [%s] for Path [%s] for Simulation [%s] already exists whilst adding Component [%s].",
                                   existingComponent.getDescription(),
                                   path.getDescription(),
                                   circuitSimulation.getDescription(),
                                   component.getDescription());
    }

    simulationMap.put(circuitSimulation, component);
  }
}

