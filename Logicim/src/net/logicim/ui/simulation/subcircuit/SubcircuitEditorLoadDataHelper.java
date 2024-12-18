package net.logicim.ui.simulation.subcircuit;

import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.CircuitLoaders;

public abstract class SubcircuitEditorLoadDataHelper
{
  public static void loadViewData(TraceView view,
                                  TraceData data,
                                  ViewPath viewPath,
                                  CircuitSimulation circuitSimulation,
                                  TraceLoader traceLoader)
  {
    data.createAndConnectComponentDuringLoad(viewPath,
                                             circuitSimulation,
                                             traceLoader,
                                             view);
  }

  public static void loadViewData(StaticView view,
                                  StaticData data,
                                  ViewPath viewPath,
                                  CircuitSimulation circuitSimulation,
                                  CircuitLoaders circuitLoaders)
  {
    data.createAndConnectComponentDuringLoad(viewPath, circuitSimulation, circuitLoaders, view);
  }
}

