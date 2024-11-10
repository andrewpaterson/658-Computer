package net.logicim.ui.circuit.path;

import net.logicim.ui.circuit.SubcircuitView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UpdatedCircuitInstanceViewPaths
{
  protected List<CircuitInstanceViewPath> newPaths;
  protected List<CircuitInstanceViewPath> removedPaths;
  protected Set<SubcircuitView> subcircuitViews;

  public UpdatedCircuitInstanceViewPaths(List<CircuitInstanceViewPath> newPaths, List<CircuitInstanceViewPath> removedPaths)
  {

    this.newPaths = newPaths;
    this.removedPaths = removedPaths;

    this.subcircuitViews = new LinkedHashSet<>();
    for (CircuitInstanceViewPath path : newPaths)
    {
      SubcircuitView subcircuitView = path.getLast().getInstanceSubcircuitView();
      subcircuitViews.add(subcircuitView);
    }

    for (CircuitInstanceViewPath path : removedPaths)
    {
      SubcircuitView subcircuitView = path.getLast().getInstanceSubcircuitView();
      subcircuitViews.add(subcircuitView);
    }
  }

  public List<CircuitInstanceViewPath> getNewPaths()
  {
    return newPaths;
  }

  public List<CircuitInstanceViewPath> getRemovedPaths()
  {
    return removedPaths;
  }

  public Set<SubcircuitView> getSubcircuitViews()
  {
    return subcircuitViews;
  }
}

