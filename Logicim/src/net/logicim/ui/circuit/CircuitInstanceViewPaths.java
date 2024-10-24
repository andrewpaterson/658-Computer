package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitObject;
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
    Map<SubcircuitSimulation, List<CircuitInstanceViewPath>> subcircuitSimulationToSourcePath = createSimulationsSet();

    createSubcircuitSimulationMap(subcircuitSimulationToSourcePath);

    validatePathsAndSimulationsMatch(subcircuitSimulationToSourcePath);

    createPathLinks();
  }

  private void validatePathsAndSimulationsMatch(Map<SubcircuitSimulation, List<CircuitInstanceViewPath>> subcircuitSimulationToSourcePath)
  {
    int activePaths = 0;
    for (CircuitInstanceViewPath path : paths)
    {
      if (path.getCircuitSimulations().size() > 0)
      {
        activePaths++;
      }
    }
    if (subcircuitSimulationToSourcePath.size() != activePaths)
    {
      throw new SimulatorException("Expected simulations size [%s] and paths size [%s] to be equal.", subcircuitSimulationToSourcePath.size(), activePaths);
    }
  }

  private void createPathsSet(List<SubcircuitEditor> subcircuitEditors)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      addSubcircuitEditor(subcircuitEditor);
    }
  }

  public void addSubcircuitEditor(SubcircuitEditor subcircuitEditor)
  {
    List<CircuitInstanceView> pathList = new ArrayList<>();
    recurseFindPaths(subcircuitEditor, pathList, null);
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

  protected void createSubcircuitSimulationMap(Map<SubcircuitSimulation, List<CircuitInstanceViewPath>> subcircuitSimulationToSourcePath)
  {
    for (Map.Entry<SubcircuitSimulation, List<CircuitInstanceViewPath>> entry : subcircuitSimulationToSourcePath.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      List<CircuitInstanceViewPath> circuitInstanceViewPaths = entry.getValue();
      for (CircuitInstanceViewPath circuitInstanceViewPath : circuitInstanceViewPaths)
      {
        if (matches(subcircuitSimulation, circuitInstanceViewPath))
        {
          if (!subcircuitSimulationPaths.containsKey(subcircuitSimulation))
          {
            subcircuitSimulationPaths.put(subcircuitSimulation, circuitInstanceViewPath);
          }
          else
          {
            throw new SimulatorException("A Path [%s] has already been added for Subcircuit Simulation [%s] trying to add another Path [%s].", subcircuitSimulationPaths.get(subcircuitSimulation).getDescription(), subcircuitSimulation.getDescription(), circuitInstanceViewPath.getDescription());
          }
        }
      }
    }

    for (Map.Entry<SubcircuitSimulation, CircuitInstanceViewPath> entry : subcircuitSimulationPaths.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      CircuitInstanceViewPath path = entry.getValue();
      path.addSubcircuitSimulation(subcircuitSimulation);
    }
  }

  private boolean matches(SubcircuitSimulation subcircuitSimulation, CircuitInstanceViewPath circuitInstanceViewPath)
  {
    CircuitInstanceView pathLast = circuitInstanceViewPath.getLast();
    if (subcircuitSimulation.isTop())
    {
      List<? extends SubcircuitSimulation> simulations = pathLast.getPathSubcircuitSimulations();
      if (simulations.size() == 1)
      {
        if (simulations.get(0) == subcircuitSimulation)
        {
          return true;
        }
        else
        {
          throw new SimulatorException("Expected exactly TopSubcircuitSimulation [%s] to match Path's [%s] last Subcircuit Simulation [%s].", subcircuitSimulation.getDescription(), circuitInstanceViewPath.getDescription(), simulations.get(0));
        }
      }
      else
      {
        throw new SimulatorException("Expected exactly [1] TopSubcircuitSimulation found [%s] for Path [%s].", simulations.size(), circuitInstanceViewPath.getDescription());
      }
    }
    else if (subcircuitSimulation.isInstance())
    {
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = (SubcircuitInstanceSimulation) subcircuitSimulation;
      SubcircuitObject firstSubcircuitObject = subcircuitInstanceSimulation.getCircuitSimulation().getSubcircuitTopSimulation().getSubcircuitObject();
      CircuitInstanceView pathFirst = circuitInstanceViewPath.getFirst();
      SubcircuitObject lastSubcircuitObject = subcircuitInstanceSimulation.getSubcircuitObject();
      return (firstSubcircuitObject == pathFirst) && (lastSubcircuitObject == pathLast);
    }
    else
    {
      throw new SimulatorException("Don't know how to deal with a SubcircuitSimulation of type [%s].", subcircuitSimulation != null ? subcircuitSimulation.getClass().getSimpleName() : null);
    }
  }

  protected Map<SubcircuitSimulation, List<CircuitInstanceViewPath>> createSimulationsSet()
  {
    Map<SubcircuitSimulation, List<CircuitInstanceViewPath>> subcircuitSimulationToSourcePath = new LinkedHashMap<>();
    for (CircuitInstanceViewPath circuitInstanceViewPath : paths)
    {
      CircuitInstanceView pathLast = circuitInstanceViewPath.getLast();
      List<? extends SubcircuitSimulation> subcircuitSimulations = pathLast.getPathSubcircuitSimulations();
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        List<CircuitInstanceViewPath> circuitInstanceViewPaths = subcircuitSimulationToSourcePath.get(subcircuitSimulation);
        if (circuitInstanceViewPaths == null)
        {
          circuitInstanceViewPaths = new ArrayList<>();
          subcircuitSimulationToSourcePath.put(subcircuitSimulation, circuitInstanceViewPaths);
        }
        circuitInstanceViewPaths.add(circuitInstanceViewPath);
      }
    }
    return subcircuitSimulationToSourcePath;
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

