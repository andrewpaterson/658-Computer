package net.logicim.ui.simulation.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.data.common.ViewData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitLoaders;

public abstract class SubcircuitEditorLoadDataHelper
{
  public static void loadViewData(View view,
                                  ViewData data,
                                  SubcircuitSimulation subcircuitSimulation,
                                  CircuitLoaders circuitLoaders)
  {
    if (view instanceof StaticView)
    {
      StaticView staticView = (StaticView) view;
      StaticData staticData = (StaticData) data;
      staticData.createAndConnectComponent(subcircuitSimulation, circuitLoaders, staticView);
    }
    else if (view instanceof TraceView)
    {
      TraceView traceView = (TraceView) view;
      TraceData traceData = (TraceData) data;
      traceData.createAndConnectComponent(subcircuitSimulation, circuitLoaders, traceView);
    }
    else
    {
      throw new SimulatorException("Cannot load component of type [%s].", view);
    }
  }
}
