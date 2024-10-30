package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.*;

public class CircuitInstanceViewPaths
{
  protected Set<CircuitInstanceViewPath> paths;
  protected Map<SubcircuitSimulation, CircuitInstanceViewPath> subcircuitSimulationPaths;

  public CircuitInstanceViewPaths(List<SubcircuitEditor> subcircuitEditors)
  {
    paths = new LinkedHashSet<>();
    subcircuitSimulationPaths = new LinkedHashMap<>();

    process(subcircuitEditors);
  }

  protected void process(List<SubcircuitEditor> subcircuitEditors)
  {
    createPathsSet(subcircuitEditors);
    createSubcircuitSimulationPaths();
    createPathLinks();
  }

  private void createSubcircuitSimulationPaths()
  {
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
  }

  private void createPathsSet(List<SubcircuitEditor> subcircuitEditors)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      recurseFindPaths(subcircuitEditor, new ArrayList<>(), null);
    }
  }

  protected void recurseFindPaths(CircuitInstanceView circuitInstanceView, List<CircuitInstanceView> path, CircuitInstanceViewPath previousViewPath)
  {
    path.add(circuitInstanceView);
    CircuitInstanceViewPath circuitInstanceViewPath = new CircuitInstanceViewPath(path);
    paths.add(circuitInstanceViewPath);

    if (previousViewPath != null)
    {
      circuitInstanceViewPath.setPrevious(previousViewPath);
    }

    SubcircuitView subcircuitView = circuitInstanceView.getInstanceSubcircuitView();
    List<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitView.getSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      recurseFindPaths(subcircuitInstanceView, path, circuitInstanceViewPath);
    }
    path.remove(path.size() - 1);
  }

  protected void createPathLinks()
  {
    for (CircuitInstanceViewPath circuitInstanceViewPath : paths)
    {
      CircuitInstanceViewPath previous = circuitInstanceViewPath.getPrevious();
      if (previous != null)
      {
        previous.setNext(circuitInstanceViewPath);
      }
    }
  }

  public List<CircuitInstanceViewPath> getPathsEndingWithSubcircuitView(SubcircuitView subcircuitView)
  {
    List<CircuitInstanceViewPath> result = new ArrayList<>();
    for (CircuitInstanceViewPath path : paths)
    {
      if (path.endsWithSubcircuitView(subcircuitView))
      {
        result.add(path);
      }
    }
    return result;
  }

  public CircuitInstanceViewPath getPath(CircuitInstanceViewPath circuitInstanceViewPath, CircuitInstanceView circuitInstanceView)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(circuitInstanceViewPath.path);
    newPath.add(circuitInstanceView);

    return getPath(newPath);
  }

  public CircuitInstanceViewPath getPathExceptLast(CircuitInstanceViewPath circuitInstanceViewPath)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(circuitInstanceViewPath.path);
    newPath.remove(newPath.size() - 1);
    return getPath(newPath);
  }

  protected CircuitInstanceViewPath getPath(List<CircuitInstanceView> newPath)
  {
    for (CircuitInstanceViewPath path : paths)
    {
      if (path.equalsPath(newPath))
      {
        return path;
      }
    }

    throw new SimulatorException("Cannot find a path matching path");
  }

  public Set<CircuitInstanceViewPath> getPaths()
  {
    return paths;
  }

  public Map<SubcircuitSimulation, CircuitInstanceViewPath> getSubcircuitSimulationPaths()
  {
    return subcircuitSimulationPaths;
  }
}

