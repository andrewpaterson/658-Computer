package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubcircuitSimulationPaths
{
  protected Map<SubcircuitSimulation, ViewPath> subcircuitSimulationPaths;

  public SubcircuitSimulationPaths(Collection<ViewPath> viewPaths)
  {
    for (ViewPath viewPath : viewPaths)
    {
      viewPath.clearCircuitSimulations();
    }
    subcircuitSimulationPaths = createSubcircuitSimulationPaths(viewPaths);
  }

  private Map<SubcircuitSimulation, ViewPath> createSubcircuitSimulationPaths(Collection<ViewPath> viewPaths)
  {
    Map<SubcircuitSimulation, ViewPath> subcircuitSimulationPaths = new LinkedHashMap<>();

    for (ViewPath viewPath : viewPaths)
    {
      List<CircuitInstanceView> circuitInstanceViews = viewPath.getPath();

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
          SubcircuitSimulation subcircuitSimulation = circuitInstanceView.getSubcircuitInstanceSimulationForParent(neededParentSimulation);
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
        subcircuitSimulationPaths.put(neededParentSimulation, viewPath);
        viewPath.addSubcircuitSimulation(neededParentSimulation);
      }
    }

    return subcircuitSimulationPaths;
  }
}

