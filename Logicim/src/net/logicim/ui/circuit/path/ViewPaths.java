package net.logicim.ui.circuit.path;

import net.common.SimulatorException;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewPaths
{
  protected List<ViewPath> viewPaths;
  protected ViewPath emptyPath;

  public ViewPaths()
  {
    viewPaths = new ArrayList<>();
    emptyPath = new ViewPath(new ArrayList<>());
    emptyPath.setEmptyId();
  }

  public ViewPaths(List<SubcircuitEditor> subcircuitEditors)
  {
    this();

    createPaths(subcircuitEditors);
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
    viewPaths.add(viewPath);

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

  public List<ViewPath> getPathsEndingWithSubcircuitView(SubcircuitView subcircuitView)
  {
    List<ViewPath> result = new ArrayList<>();
    for (ViewPath viewPath : viewPaths)
    {
      if (viewPath.endsWithSubcircuitView(subcircuitView))
      {
        result.add(viewPath);
      }
    }
    return result;
  }

  public ViewPath getViewPath(ViewPath viewPath, CircuitInstanceView circuitInstanceView)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(viewPath.path);
    newPath.add(circuitInstanceView);

    return getViewPath(newPath);
  }

  public ViewPath getPathExceptLast(ViewPath viewPath)
  {
    List<CircuitInstanceView> newPath = new ArrayList<>(viewPath.path);
    newPath.remove(newPath.size() - 1);
    return getViewPath(newPath);
  }

  private ViewPath getViewPath(List<CircuitInstanceView> newPath)
  {
    for (ViewPath viewPath : viewPaths)
    {
      if (viewPath.equalsPath(newPath))
      {
        return viewPath;
      }
    }

    throw new SimulatorException("Cannot find a path matching path [%s].", ViewPath.toPathString(newPath));
  }

  public List<ViewPath> getViewPaths()
  {
    return viewPaths;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (ViewPath viewPath : viewPaths)
    {
      builder.append(viewPath.getDescription());
      builder.append("\n");
    }
    return builder.toString();
  }

  public boolean addIfNotPresent(ViewPath newPath)
  {
    for (ViewPath viewPath : viewPaths)
    {
      if (viewPath.equals(newPath))
      {
        return false;
      }
    }

    viewPaths.add(newPath);
    return true;
  }

  public boolean contains(ViewPath newPath)
  {
    for (ViewPath existingPath : viewPaths)
    {
      if (existingPath.equals(newPath))
      {
        return true;
      }
    }
    return false;
  }

  public ViewPath getViewPath(int index)
  {
    return viewPaths.get(index);
  }

  public void removePath(int index)
  {
    viewPaths.remove(index);
  }

  public boolean matches(ViewPaths other)
  {
    if (other.getViewPaths().size() != viewPaths.size())
    {
      return false;
    }

    List<ViewPath> otherPaths = new ArrayList<>(other.getViewPaths());
    List<ViewPath> thisPaths = new ArrayList<>(viewPaths);

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

  public ViewPath getEmptyPath()
  {
    return emptyPath;
  }
}

