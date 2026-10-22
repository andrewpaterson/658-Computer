package net.logicim.ui.circuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Traces;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.WireViewCompAccessor;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisconnectedWireFinder
{
  //  Every TraceView is checked on every ViewPath that ends in its SubcircuitView (one ViewPath per sub-circuit
  //  "instance") and for every CircuitSimulation that ViewPath carries.  A wire is reported if it has no Traces
  //  for that path and simulation, or if it would be painted with the disconnected colour.
  public static List<String> findDisconnectedWires(CircuitEditor circuitEditor)
  {
    List<String> disconnectedWires = new ArrayList<>();
    Color disconnectedColour = Colours.getInstance().getDisconnectedTrace();
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
              String problem = findProblem(traceView, viewPath, circuitSimulation, disconnectedColour);
              if (problem != null)
              {
                disconnectedWires.add(subcircuitView.getTypeName() + ": " +
                                      traceView.getDescription() + " on path [" +
                                      viewPath.getDescription() + "] in simulation [" +
                                      circuitSimulation.getDescription() + "] " + problem);
              }
            }
          }
        }
      }
    }
    return disconnectedWires;
  }

  private static String findProblem(TraceView traceView, ViewPath viewPath, CircuitSimulation circuitSimulation, Color disconnectedColour)
  {
    Traces traces = traceView.getTraces(viewPath, circuitSimulation);
    if (traces == null)
    {
      return "has no traces.";
    }
    if (traces.getTraces().isEmpty())
    {
      return "has an empty trace set.";
    }

    SubcircuitSimulation subcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    Color colour = WireViewCompAccessor.getTraceColour(traceView, subcircuitSimulation);
    if (disconnectedColour.equals(colour))
    {
      return "is painted disconnected.";
    }
    return null;
  }

  public static String toReport(List<String> disconnectedWires)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(disconnectedWires.size()).append(" disconnected wire(s):\n");
    for (String disconnectedWire : disconnectedWires)
    {
      builder.append("  ").append(disconnectedWire).append("\n");
    }
    return builder.toString();
  }
}
