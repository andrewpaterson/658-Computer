package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.*;

public class SubcircuitSimulationPaths
{
  protected Map<SubcircuitSimulation, CircuitInstanceViewPath> subcircuitSimulationPaths;

  public SubcircuitSimulationPaths(Collection<CircuitInstanceViewPath> paths)
  {
    for (CircuitInstanceViewPath path : paths)
    {
      path.clearCircuitSimulations();
    }
    subcircuitSimulationPaths = createSubcircuitSimulationPaths(paths);
  }

  private Map<SubcircuitSimulation, CircuitInstanceViewPath> createSubcircuitSimulationPaths(Collection<CircuitInstanceViewPath> paths)
  {
    Map<SubcircuitSimulation, CircuitInstanceViewPath> subcircuitSimulationPaths = new LinkedHashMap<>();

    for (CircuitInstanceViewPath path : paths)
    {
      List<CircuitInstanceView> circuitInstanceViews = path.getPath();

      SubcircuitSimulation neededParentSimulation = null;
      for (CircuitInstanceView circuitInstanceView : circuitInstanceViews)
      {
        if (neededParentSimulation == null)
        {
          List<? extends SubcircuitSimulation> instanceSubcircuitSimulations = circuitInstanceView.getInstanceSubcircuitSimulations();
          if (instanceSubcircuitSimulations.size() == 1)
          {
            SubcircuitSimulation subcircuitSimulation = instanceSubcircuitSimulations.get(0);
            if (subcircuitSimulation.isTop())
            {
              neededParentSimulation = subcircuitSimulation;
            }
            else
            {
              throw new SimulatorException("The first parent Simulation [%s] must be a Top Simulation.", subcircuitSimulation.getDescription());
            }
          }
          else
          {
            throw new SimulatorException("You need to deal with multiple CircuitSimulations still.");
          }
        }
        else
        {
          SubcircuitSimulation subcircuitSimulation = circuitInstanceView.getSubcircuitSimulationForParent(neededParentSimulation);
          if (subcircuitSimulation != null)
          {
            if (subcircuitSimulation.isInstance())
            {
              neededParentSimulation = subcircuitSimulation;
            }
            else
            {
              throw new SimulatorException("The child Simulation [%s] of parent Simulation [%s] must be an Instance Simulation.", subcircuitSimulation.getDescription(), neededParentSimulation.getDescription());
            }
          }
          else
          {
            neededParentSimulation = null;
            break;
          }
        }
      }

      if (neededParentSimulation != null)
      {
        subcircuitSimulationPaths.put(neededParentSimulation, path);
        path.addSubcircuitSimulation(neededParentSimulation);
      }
    }

    return subcircuitSimulationPaths;
  }

  public Map<SubcircuitSimulation, CircuitInstanceViewPath> getSubcircuitSimulationPaths()
  {
    return subcircuitSimulationPaths;
  }
}

