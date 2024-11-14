package net.logicim.ui.circuit.path;

import net.logicim.ui.circuit.SubcircuitView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UpdatedViewPaths
{
  protected List<ViewPath> newPaths;
  protected List<ViewPath> removedPaths;
  protected Set<SubcircuitView> subcircuitViews;

  public UpdatedViewPaths(List<ViewPath> newPaths, List<ViewPath> removedPaths)
  {

    this.newPaths = newPaths;
    this.removedPaths = removedPaths;

    this.subcircuitViews = new LinkedHashSet<>();
    for (ViewPath path : newPaths)
    {
      SubcircuitView subcircuitView = path.getLast().getInstanceSubcircuitView();
      subcircuitViews.add(subcircuitView);
    }

    for (ViewPath path : removedPaths)
    {
      SubcircuitView subcircuitView = path.getLast().getInstanceSubcircuitView();
      subcircuitViews.add(subcircuitView);
    }
  }

  public List<ViewPath> getNewPaths()
  {
    return newPaths;
  }

  public List<ViewPath> getRemovedPaths()
  {
    return removedPaths;
  }

  public Set<SubcircuitView> getSubcircuitViews()
  {
    return subcircuitViews;
  }
}

