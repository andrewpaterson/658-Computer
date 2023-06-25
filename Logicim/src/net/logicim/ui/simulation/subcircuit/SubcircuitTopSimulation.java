package net.logicim.ui.simulation.subcircuit;

import net.logicim.common.util.Counter;
import net.logicim.data.simulation.SubcircuitTopSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceViewParent;
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


  public List<CircuitInstanceViewParent> getTopDownSubcircuitViews()
  {
    List<CircuitInstanceViewParent> circuitInstanceViewParents = new ArrayList<>();

    Counter counter = new Counter();
    CircuitInstanceViewParent instanceViewParent = new CircuitInstanceViewParent(null, subcircuitEditor, counter.tick());
    circuitInstanceViewParents.add(instanceViewParent);
    recurseFindSubCircuitViews(instanceViewParent, circuitInstanceViewParents, counter);
    return circuitInstanceViewParents;
  }

  protected void recurseFindSubCircuitViews(CircuitInstanceViewParent circuitInstanceViewParent, List<CircuitInstanceViewParent> circuitInstanceViewParents, Counter counter)
  {
    SubcircuitView subcircuitView = circuitInstanceViewParent.getCircuitSubcircuitView();
    Set<SubcircuitInstanceView> instanceViews = subcircuitView.findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView instanceView : instanceViews)
    {
      CircuitInstanceViewParent instanceViewParent = new CircuitInstanceViewParent(circuitInstanceViewParent, instanceView, counter.tick());
      circuitInstanceViewParents.add(instanceViewParent);
      recurseFindSubCircuitViews(instanceViewParent, circuitInstanceViewParents, counter);
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

