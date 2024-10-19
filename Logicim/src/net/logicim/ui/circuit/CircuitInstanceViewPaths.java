package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.*;

public class CircuitInstanceViewPaths
{
  protected Set<CircuitInstanceViewPath> paths;
  protected Map<SubcircuitSimulation, CircuitInstanceViewPath> subcircuitSimulations;

  public CircuitInstanceViewPaths(List<SubcircuitEditor> subcircuitEditors)
  {
    paths = new LinkedHashSet<>();
    subcircuitSimulations = new LinkedHashMap<>();

    process(subcircuitEditors);
  }

  protected void process(List<SubcircuitEditor> subcircuitEditors)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      addSubcircuitEditor(subcircuitEditor);
    }

    createSubcircuitSimulationMap();
    createPathLinks();
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

    SubcircuitView subcircuitView = circuitInstanceView.getCircuitSubcircuitView();
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
        CircuitInstanceViewPath existing = this.subcircuitSimulations.get(subcircuitSimulation);
        if (existing != null)
        {
          throw new SimulatorException("More than one subcircuit simulations exists.");
        }

        this.subcircuitSimulations.put(subcircuitSimulation, circuitInstanceViewPath);
      }
    }

    for (Map.Entry<SubcircuitSimulation, CircuitInstanceViewPath> entry : subcircuitSimulations.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      CircuitInstanceViewPath path = entry.getValue();
      path.addSubcircuitSimulation(subcircuitSimulation);
    }
  }

  protected void createPathLinks()
  {
    Collection<CircuitInstanceViewPath> circuitInstanceViewPaths = subcircuitSimulations.values();
    for (CircuitInstanceViewPath circuitInstanceViewPath : circuitInstanceViewPaths)
    {
      CircuitInstanceView secondLast = circuitInstanceViewPath.getSecondLast();
      if (secondLast != null)
      {
        SubcircuitSimulations simulations = secondLast.getCircuitSubcircuitView().getSimulations();
        for (SubcircuitSimulation simulation : simulations.getSubcircuitSimulations())
        {
          CircuitInstanceViewPath secondLastPath = subcircuitSimulations.get(simulation);
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

  public CircuitInstanceViewPath getFirst(CircuitInstanceView circuitInstanceView)
  {
    for (CircuitInstanceViewPath path : paths)
    {
      if (path.size() == 1)
      {
        if (path.getPath().get(0) == circuitInstanceView)
        {
          return path;
        }
      }
    }
    throw new SimulatorException("Cannot get first path for [%s].", circuitInstanceView.getDescription());
  }

  public CircuitInstanceViewPath getPath(CircuitInstanceViewPath circuitInstanceViewPath, CircuitInstanceView circuitInstanceView)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(circuitInstanceViewPath.path);
    newPath.add(circuitInstanceView);

    return getPath(newPath);
  }

  public CircuitInstanceViewPath getPathExceptLast(CircuitInstanceViewPath circuitInstanceViewPath, SubcircuitInstanceView expectedFinalPathElement)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(circuitInstanceViewPath.path);
    newPath.remove(newPath.size() - 1);

    CircuitInstanceView actualFinalPathElement = newPath.get(newPath.size() - 1);
    if (actualFinalPathElement != expectedFinalPathElement)
    {
      throw new SimulatorException("Expected final path element [%s] was not equal to final path element [%s].",
                                   expectedFinalPathElement.getDescription(),
                                   actualFinalPathElement.getDescription());
    }

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

  public Map<SubcircuitSimulation, CircuitInstanceViewPath> getSubcircuitSimulations()
  {
    return subcircuitSimulations;
  }

  public SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation, CircuitInstanceViewPath path)
  {
    SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
    if (subcircuitSimulation == null)
    {
      throw new SimulatorException("Path does not contain CircuitSimulation.");
    }
    return subcircuitSimulation;
  }
}

