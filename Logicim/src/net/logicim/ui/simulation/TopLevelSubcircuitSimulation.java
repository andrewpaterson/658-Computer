package net.logicim.ui.simulation;

import net.logicim.data.circuit.TimelineData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Timeline;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TopLevelSubcircuitSimulation
{
  protected SubcircuitEditor subcircuitEditor;
  protected CircuitSimulation circuitSimulation;

  public TopLevelSubcircuitSimulation(SubcircuitEditor subcircuitEditor, CircuitSimulation circuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.circuitSimulation = circuitSimulation;
  }

  public SubcircuitEditor getSubcircuitEditor()
  {
    return subcircuitEditor;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }

  public CircuitSimulationData save()
  {
    Timeline timeline = circuitSimulation.getTimeline();
    TimelineData timelineData = timeline.save();
    return new CircuitSimulationData(timelineData,
                                     circuitSimulation.getId(),
                                     circuitSimulation.getName(),
                                     subcircuitEditor.getId());
  }

  @Override
  public String toString()
  {
    return circuitSimulation.getDescription();
  }

  public List<SubcircuitView> getTopDownSubcircuitViews()
  {
    SubcircuitView subcircuitView = subcircuitEditor.getSubcircuitView();
    List<SubcircuitView> subcircuitViews = new ArrayList<>();
    recurseFindSubCircuitViews(subcircuitViews, subcircuitView);
    return subcircuitViews;
  }

  protected void recurseFindSubCircuitViews(List<SubcircuitView> subcircuitViews, SubcircuitView subcircuitView)
  {
    subcircuitViews.add(subcircuitView);
    Set<SubcircuitInstanceView> instanceViews = subcircuitView.findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView instanceView : instanceViews)
    {
      SubcircuitView instanceSubcircuitView = instanceView.getInstanceSubcircuitView();
      recurseFindSubCircuitViews(subcircuitViews, instanceSubcircuitView);
    }
  }
}

