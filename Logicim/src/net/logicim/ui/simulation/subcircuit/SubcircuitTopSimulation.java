package net.logicim.ui.simulation.subcircuit;

import net.logicim.data.simulation.SubcircuitTopSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubcircuitTopSimulation
    extends SubcircuitSimulation
{
  public SubcircuitTopSimulation(CircuitSimulation circuitSimulation, SubcircuitEditor subcircuitEditor)
  {
    super(circuitSimulation, subcircuitEditor);
  }

  public SubcircuitTopSimulation(CircuitSimulation circuitSimulation, SubcircuitEditor subcircuitEditor, long id)
  {
    super(circuitSimulation, subcircuitEditor, id);
  }

  @Override
  public SubcircuitTopSimulationData save()
  {
    return new SubcircuitTopSimulationData(getId(),
                                           circuitSimulation.getId(),
                                           subcircuitEditor.getId());
  }

  public List<CircuitInstanceViewPath> getTopDownSubcircuitViews()
  {
    List<CircuitInstanceViewPath> circuitPaths = new ArrayList<>();
    List<CircuitInstanceView> path = new ArrayList<>();
    path.add(subcircuitEditor);
    recurseFindSubCircuitViews(path,
                               circuitPaths,
                               subcircuitEditor.getSubcircuitView());
    return circuitPaths;
  }

  protected void recurseFindSubCircuitViews(List<CircuitInstanceView> path, List<CircuitInstanceViewPath> circuitPaths, SubcircuitView subcircuitView)
  {
    Set<SubcircuitInstanceView> instanceViews = subcircuitView.findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView instanceView : instanceViews)
    {
      path.add(instanceView);
      circuitPaths.add(new CircuitInstanceViewPath(path));
      SubcircuitView instanceSubcircuitView = instanceView.getInstanceSubcircuitView();
      recurseFindSubCircuitViews(path, circuitPaths, instanceSubcircuitView);
      path.remove(path.size() - 1);
    }
  }

  @Override
  public String toString()
  {
    if (circuitSimulation != null)
    {
      return circuitSimulation.getDescription();
    }
    else
    {
      return "";
    }
  }

  @Override
  public SubcircuitInstance getSubcircuitInstance()
  {
    return null;
  }

  public void reset()
  {

  }
}

