package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.*;

public class ViewPaths
{
  protected List<ViewPath> paths;

  public ViewPaths()
  {
    this.paths = new ArrayList<>();
  }

  public ViewPaths(List<SubcircuitEditor> subcircuitEditors)
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

  private boolean recurseFindPaths(CircuitInstanceView circuitInstanceView, List<CircuitInstanceView> path, ViewPath previousViewPath, int depth)
  {
    if (depth > 100)
    {
      return true;
    }

    path.add(circuitInstanceView);
    ViewPath viewPath = new ViewPath(path);
    paths.add(viewPath);

    if (previousViewPath != null)
    {
      viewPath.setPrevious(previousViewPath);
    }

    SubcircuitView subcircuitView = circuitInstanceView.getInstanceSubcircuitView();
    List<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitView.getSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      boolean depthExceeded = recurseFindPaths(subcircuitInstanceView, path, viewPath, depth + 1);
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
    for (ViewPath viewPath : paths)
    {
      ViewPath previous = viewPath.getPrevious();
      if (previous != null)
      {
        previous.setNext(viewPath);
      }
    }
  }

  public List<ViewPath> getPathsEndingWithSubcircuitView(SubcircuitView subcircuitView)
  {
    List<ViewPath> result = new ArrayList<>();
    for (ViewPath path : paths)
    {
      if (path.endsWithSubcircuitView(subcircuitView))
      {
        result.add(path);
      }
    }
    return result;
  }

  public ViewPath getPath(ViewPath viewPath, CircuitInstanceView circuitInstanceView)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(viewPath.path);
    newPath.add(circuitInstanceView);

    return getPath(newPath);
  }

  public ViewPath getPathExceptLast(ViewPath viewPath)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(viewPath.path);
    newPath.remove(newPath.size() - 1);
    return getPath(newPath);
  }

  private ViewPath getPath(List<CircuitInstanceView> newPath)
  {
    for (ViewPath path : paths)
    {
      if (path.equalsPath(newPath))
      {
        return path;
      }
    }

    throw new SimulatorException("Cannot find a path matching path");
  }

  public List<ViewPath> getPaths()
  {
    return paths;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (ViewPath path : paths)
    {
      builder.append(path.getDescription());
      builder.append("\n");
    }
    return builder.toString();
  }

  public boolean addIfNotPresent(ViewPath newPath)
  {
    for (ViewPath path : paths)
    {
      if (path.equals(newPath))
      {
        return false;
      }
    }

    paths.add(newPath);
    return true;
  }

  public boolean contains(ViewPath newPath)
  {
    for (ViewPath existingPath : paths)
    {
      if (existingPath.equals(newPath))
      {
        return true;
      }
    }
    return false;
  }

  public ViewPath getPath(int index)
  {
    return paths.get(index);
  }

  public void removePath(int index)
  {
    paths.remove(index);
  }

  public boolean matches(ViewPaths other)
  {
    if (other.getPaths().size() != paths.size())
    {
      return false;
    }

    List<ViewPath> otherPaths = new ArrayList<>(other.getPaths());
    List<ViewPath> thisPaths = new ArrayList<>(paths);

    Collections.sort(otherPaths);
    Collections.sort(thisPaths);

    int count = thisPaths.size();
    for (int i = 0; i < count; i++)
    {
      ViewPath thisPath = thisPaths.get(i);
      ViewPath otherPath = otherPaths.get(i);

      if (!thisPath.equals(otherPath))
      {
        return false;
      }
    }

    return true;
  }
}

