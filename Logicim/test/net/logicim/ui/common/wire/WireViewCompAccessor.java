package net.logicim.ui.common.wire;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.awt.*;

public class WireViewCompAccessor
{
  public static Color getTraceColour(TraceView traceView, SubcircuitSimulation subcircuitSimulation)
  {
    return traceView.getTraceColour(subcircuitSimulation);
  }
}
