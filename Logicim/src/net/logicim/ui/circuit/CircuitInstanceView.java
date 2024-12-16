package net.logicim.ui.circuit;

import net.common.util.Counter;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.order.CircuitInstanceOrderer;
import net.logicim.ui.circuit.order.CircuitInstanceViewParent;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CircuitInstanceView
{
  SubcircuitView getInstanceSubcircuitView();

  String getDescription();

  Collection<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations(CircuitSimulation circuitSimulation);

  List<? extends SubcircuitSimulation> getInstanceSubcircuitSimulations();

  SubcircuitSimulation getSubcircuitInstanceSimulationForParent(SubcircuitSimulation parentSubcircuitSimulation);

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

  long getId();

  String getType();
}

