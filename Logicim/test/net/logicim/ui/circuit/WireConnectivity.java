package net.logicim.ui.circuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.*;

//  Describes which wires share Traces, for every wire on every ViewPath in every CircuitSimulation.  Traces are
//  labelled in the order they are first seen in the sorted wire descriptions, so two CircuitEditors that connect the
//  same wires together produce the same text even though their Trace objects differ.
public class WireConnectivity
{
  public static String describe(CircuitEditor circuitEditor)
  {
    Map<String, List<Trace>> wireTraces = new TreeMap<>();
    List<ViewPath> viewPaths = circuitEditor.getViewPaths().getViewPaths();
    for (SubcircuitEditor subcircuitEditor : circuitEditor.getSubcircuitEditors())
    {
      SubcircuitView subcircuitView = subcircuitEditor.getInstanceSubcircuitView();
      for (TraceView traceView : subcircuitView.getTraceViews())
      {
        for (ViewPath viewPath : viewPaths)
        {
          if (viewPath.endsWithSubcircuitView(subcircuitView))
          {
            for (CircuitSimulation circuitSimulation : viewPath.getCircuitSimulations())
            {
              String key = subcircuitView.getTypeName() + ": " +
                           traceView.getDescription() + " on path [" +
                           viewPath.getDescription() + "] in simulation [" +
                           circuitSimulation.getDescription() + "]";
              Traces traces = traceView.getTraces(viewPath, circuitSimulation);
              List<Trace> traceList = new ArrayList<>();
              if (traces != null)
              {
                traceList.addAll(traces.getTraces());
              }
              wireTraces.put(key, traceList);
            }
          }
        }
      }
    }

    Map<Trace, Integer> labels = new HashMap<>();
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, List<Trace>> entry : wireTraces.entrySet())
    {
      builder.append(entry.getKey()).append(" =");
      List<Trace> traceList = entry.getValue();
      if (traceList.isEmpty())
      {
        builder.append(" none");
      }
      for (Trace trace : traceList)
      {
        Integer label = labels.get(trace);
        if (label == null)
        {
          label = labels.size() + 1;
          labels.put(trace, label);
        }
        builder.append(" T").append(label);
      }
      builder.append("\n");
    }
    return builder.toString();
  }
}
