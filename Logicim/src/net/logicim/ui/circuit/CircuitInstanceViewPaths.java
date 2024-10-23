package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.*;

public class CircuitInstanceViewPaths
{
  protected Set<CircuitInstanceViewPath> paths;
  protected Map<SubcircuitSimulation, CircuitInstanceViewPath> subcircuitSimulationPaths;
  protected Set<SubcircuitSimulation> subcircuitSimulations;

  public CircuitInstanceViewPaths(List<SubcircuitEditor> subcircuitEditors)
  {
    paths = new LinkedHashSet<>();
    subcircuitSimulationPaths = new LinkedHashMap<>();
    subcircuitSimulations = new LinkedHashSet<>();

    process(subcircuitEditors);
  }

  protected void process(List<SubcircuitEditor> subcircuitEditors)
  {
    createPathsSet(subcircuitEditors);
    createSimulationsSet();

    if (subcircuitSimulations.size() != paths.size())
    {
      throw new SimulatorException("Expected simulations size [%s] and paths size [%s] to be equal.", subcircuitSimulations.size(), paths.size());
    }

    createSubcircuitSimulationMap();
    createPathLinks();
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
    recurseFindPaths(subcircuitEditor, pathList);
  }

  protected void recurseFindPaths(CircuitInstanceView circuitInstanceView, List<CircuitInstanceView> path)
  {
    path.add(circuitInstanceView);
    paths.add(new CircuitInstanceViewPath(path));

    SubcircuitView subcircuitView = circuitInstanceView.getInstanceSubcircuitView();
    List<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitView.getSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      recurseFindPaths(subcircuitInstanceView, path);
    }
    path.remove(path.size() - 1);
  }

  protected void createSubcircuitSimulationMap()
  {
    for (CircuitInstanceViewPath circuitInstanceViewPath : paths)
    {
      CircuitInstanceView pathLast = circuitInstanceViewPath.getLast();

      List<? extends SubcircuitSimulation> subcircuitSimulations = pathLast.getPathSubcircuitSimulations();
      for (SubcircuitSimulation subcircuitSimulation : subcircuitSimulations)
      {
        CircuitInstanceViewPath existing = subcircuitSimulationPaths.get(subcircuitSimulation);
        if (existing != null)
        {
          throw new SimulatorException("More than one subcircuit simulations exists.");
        }

        subcircuitSimulationPaths.put(subcircuitSimulation, circuitInstanceViewPath);
      }
    }

    for (Map.Entry<SubcircuitSimulation, CircuitInstanceViewPath> entry : subcircuitSimulationPaths.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      CircuitInstanceViewPath path = entry.getValue();
      path.addSubcircuitSimulation(subcircuitSimulation);
    }
  }

  protected void createSimulationsSet()
  {
    for (CircuitInstanceViewPath circuitInstanceViewPath : paths)
    {
      CircuitInstanceView pathLast = circuitInstanceViewPath.getLast();
      subcircuitSimulations.addAll(pathLast.getPathSubcircuitSimulations());
    }
  }

  protected void createPathLinks()
  {
    Collection<CircuitInstanceViewPath> circuitInstanceViewPaths = subcircuitSimulationPaths.values();
    for (CircuitInstanceViewPath circuitInstanceViewPath : circuitInstanceViewPaths)
    {
      CircuitInstanceView secondLast = circuitInstanceViewPath.getSecondLast();
      if (secondLast != null)
      {
        SubcircuitSimulations simulations = secondLast.getInstanceSubcircuitView().getSimulations();
        for (SubcircuitSimulation simulation : simulations.getSubcircuitSimulations())
        {
          CircuitInstanceViewPath secondLastPath = subcircuitSimulationPaths.get(simulation);
          circuitInstanceViewPath.setPrevious(secondLastPath);
        }
      }
    }

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

