package net.logicim.ui.circuit;

import net.logicim.common.util.Counter;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface CircuitInstanceView
{
  SubcircuitView getCircuitSubcircuitView();

  String getDescription();

  List<SubcircuitInstanceSimulation> getInnerSubcircuitSimulations(CircuitSimulation circuitSimulation);

  default List<CircuitInstanceView> getOrderedCircuitInstanceViews()
  {
    List<CircuitInstanceViewParent> viewParents = getCircuitInstanceViews();

    CircuitInstanceOrderer orderer = new CircuitInstanceOrderer(viewParents);
    List<CircuitInstanceViewParent> ordered = orderer.order();
    List<CircuitInstanceView> circuitInstanceViews = new ArrayList<>();
    for (CircuitInstanceViewParent circuitInstanceViewParent : ordered)
    {
      circuitInstanceViews.add(circuitInstanceViewParent.getView());
    }
    return circuitInstanceViews;
  }

  private List<CircuitInstanceViewParent> getCircuitInstanceViews()
  {
    List<CircuitInstanceViewParent> circuitInstanceViews = new ArrayList<>();

    Counter counter = new Counter();
    CircuitInstanceViewParent instanceViewParent = new CircuitInstanceViewParent(null, this, counter.tick());
    circuitInstanceViews.add(instanceViewParent);
    recurseFindSubCircuitViews(instanceViewParent, circuitInstanceViews, counter);
    return circuitInstanceViews;
  }

  private void recurseFindSubCircuitViews(CircuitInstanceViewParent circuitInstanceViewParent, List<CircuitInstanceViewParent> circuitInstanceViewParents, Counter counter)
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
}

