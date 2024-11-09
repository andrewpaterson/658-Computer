package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.ArrayList;
import java.util.List;

public class CircuitInstanceViewPaths
{
  protected List<CircuitInstanceViewPath> paths;

  public CircuitInstanceViewPaths(List<SubcircuitEditor> subcircuitEditors)
  {
    paths = new ArrayList<>();

    createPaths(subcircuitEditors);
    createPathLinks();
  }

  private void createPaths(List<SubcircuitEditor> subcircuitEditors)
  {

    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      boolean depthExceeded = recurseFindPaths(subcircuitEditor, new ArrayList<>(), null, 0);
      if (depthExceeded)
      {
        throw new SimulatorException("Maximum path depth [%s] exceeded.", 100);
      }
    }
  }

  private boolean recurseFindPaths(CircuitInstanceView circuitInstanceView, List<CircuitInstanceView> path, CircuitInstanceViewPath previousViewPath, int depth)
  {
    if (depth > 100)
    {
      return true;
    }

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
      boolean depthExceeded = recurseFindPaths(subcircuitInstanceView, path, circuitInstanceViewPath, depth + 1);
      if (depthExceeded)
      {
        return true;
      }
    }

    path.remove(path.size() - 1);
    return false;
  }

  public void createPathLinks()
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

  private CircuitInstanceViewPath getPath(List<CircuitInstanceView> newPath)
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

  public List<CircuitInstanceViewPath> getPaths()
  {
    return paths;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (CircuitInstanceViewPath path : paths)
    {
      builder.append(path.getDescription());
      builder.append("\n");
    }
    return builder.toString();
  }

  public void addIfNotPresent(CircuitInstanceViewPath newPath)
  {
    for (CircuitInstanceViewPath path : paths)
    {
      if (path.equals(newPath))
      {
        return;
      }
    }

    paths.add(newPath);
  }

  public boolean contains(CircuitInstanceViewPath newPath)
  {
    for (CircuitInstanceViewPath existingPath : paths)
    {
      if (existingPath.equals(newPath))
      {
        return true;
      }
    }
    return false;
  }

  public CircuitInstanceViewPath getPath(int index)
  {
    return paths.get(index);
  }

  public void removePath(int index)
  {
    paths.remove(index);
  }
}

