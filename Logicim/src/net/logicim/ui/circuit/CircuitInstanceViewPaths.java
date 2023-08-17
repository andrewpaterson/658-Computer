package net.logicim.ui.circuit;

import net.logicim.common.SimulatorException;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CircuitInstanceViewPaths
{
  protected Set<CircuitInstanceViewPath> paths;

  public CircuitInstanceViewPaths()
  {
    paths = new LinkedHashSet<>();
  }

  public void process(CircuitInstanceView circuitInstanceView)
  {
    List<CircuitInstanceView> path = new ArrayList<>();
    recurseFindPaths(circuitInstanceView, path);
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
  }

  public CircuitInstanceViewPath getFirst()
  {
    return paths.iterator().next();
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
}

