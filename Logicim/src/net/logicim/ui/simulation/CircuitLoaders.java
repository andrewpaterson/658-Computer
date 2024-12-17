package net.logicim.ui.simulation;

import net.logicim.data.circuit.ViewPathCircuitSimulationLoader;
import net.logicim.data.simulation.SimulationLoader;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.HashMap;
import java.util.Map;

public class CircuitLoaders
{
  public TraceLoader traceLoader;
  public SimulationLoader simulationLoader;
  public ViewPathCircuitSimulationLoader viewPathLoader;

  public Map<Long, SubcircuitEditor> subcircuitEditorMap;
  public Map<Long, StaticView<?>> staticMap;
  public Map<Long, SubcircuitInstanceView> subcircuitInstanceViewMap;
  public Map<SubcircuitEditor, DataViewMap> subcircuitEditorViews;
  public Map<Long, ViewPath> viewPathMap;

  public CircuitLoaders()
  {
    traceLoader = new TraceLoader();
    simulationLoader = new SimulationLoader();
    viewPathLoader = new ViewPathCircuitSimulationLoader();

    subcircuitEditorMap = new HashMap<>();
    staticMap = new HashMap<>();
    subcircuitInstanceViewMap = new HashMap<>();
    subcircuitEditorViews = new HashMap<>();
    viewPathMap = new HashMap<>();
  }

  public CircuitSimulation getCircuitSimulation(long circuitSimulationId)
  {
    return simulationLoader.getCircuitSimulation(circuitSimulationId);
  }

  public SimulationLoader getSimulationLoader()
  {
    return simulationLoader;
  }

  public TraceLoader getTraceLoader()
  {
    return traceLoader;
  }

  public ViewPathCircuitSimulationLoader getViewPathLoader()
  {
    return viewPathLoader;
  }

  public Map<Long, SubcircuitEditor> getSubcircuitEditorMap()
  {
    return subcircuitEditorMap;
  }

  public Map<Long, StaticView<?>> getStaticMap()
  {
    return staticMap;
  }

  public Map<Long, SubcircuitInstanceView> getSubcircuitInstanceViewMap()
  {
    return subcircuitInstanceViewMap;
  }

  public Map<SubcircuitEditor, DataViewMap> getSubcircuitEditorViews()
  {
    return subcircuitEditorViews;
  }

  public Map<Long, ViewPath> getViewPathMap()
  {
    return viewPathMap;
  }
}

