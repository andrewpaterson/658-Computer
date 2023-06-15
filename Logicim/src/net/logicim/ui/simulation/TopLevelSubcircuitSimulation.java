package net.logicim.ui.simulation;

import net.logicim.data.circuit.TimelineData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.domain.common.Timeline;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TopLevelSubcircuitSimulation
{
  protected SubcircuitEditor subcircuitEditor;
  protected InstanceCircuitSimulation instanceCircuitSimulation;

  public TopLevelSubcircuitSimulation(SubcircuitEditor subcircuitEditor,
                                      InstanceCircuitSimulation instanceCircuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.instanceCircuitSimulation = instanceCircuitSimulation;
  }

  public SubcircuitEditor getSubcircuitEditor()
  {
    return subcircuitEditor;
  }

  public InstanceCircuitSimulation getInstanceCircuitSimulation()
  {
    return instanceCircuitSimulation;
  }

  public CircuitSimulationData save()
  {
    Timeline timeline = instanceCircuitSimulation.getTimeline();
    TimelineData timelineData = timeline.save();
    return new CircuitSimulationData(timelineData,
                                     instanceCircuitSimulation.getId(),
                                     instanceCircuitSimulation.getName(),
                                     subcircuitEditor.getId());
  }

  @Override
  public String toString()
  {
    return instanceCircuitSimulation.getDescription();
  }

  public List<CircuitInstanceViewPath> getTopDownSubcircuitViews()
  {
    SubcircuitView subcircuitView = subcircuitEditor.getSubcircuitView();
    List<CircuitInstanceViewPath> circuitPaths = new ArrayList<>();
    List<CircuitInstanceView> path = new ArrayList<>();
    path.add(subcircuitView);
    recurseFindSubCircuitViews(path, circuitPaths, subcircuitView);
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
}

