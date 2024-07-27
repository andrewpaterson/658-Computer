package net.logicim.ui.simulation.subcircuit;

import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitLoaders;

public abstract class SubcircuitEditorLoadDataHelper
{
  public static void loadViewData(TraceView view,
                                  TraceData data,
                                  SubcircuitSimulation subcircuitSimulation,
                                  TraceLoader traceLoader)
  {
    data.createAndConnectComponent(subcircuitSimulation, traceLoader, view);
  }

  public static void loadViewData(StaticView view,
                                  StaticData data,
                                  SubcircuitSimulation subcircuitSimulation,
                                  CircuitLoaders circuitLoaders)
  {
    data.createAndConnectComponent(subcircuitSimulation, circuitLoaders, view);
  }
}

