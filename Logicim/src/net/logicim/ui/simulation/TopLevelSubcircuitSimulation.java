package net.logicim.ui.simulation;

import net.logicim.data.circuit.TimelineData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Timeline;

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
}

