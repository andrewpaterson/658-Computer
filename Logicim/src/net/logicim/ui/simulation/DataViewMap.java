package net.logicim.ui.simulation;

import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.Map;

public class DataViewMap
{
  public Map<TraceData, TraceView> traceViews;
  public Map<StaticData<?>, StaticView<?>> staticViews;
  public Map<SubcircuitInstanceData, SubcircuitInstanceView> subcircuitInstanceViews;

  public DataViewMap(Map<TraceData, TraceView> traceViews,
                     Map<StaticData<?>, StaticView<?>> staticViews,
                     Map<SubcircuitInstanceData, SubcircuitInstanceView> subcircuitInstanceViews)
  {
    this.traceViews = traceViews;
    this.staticViews = staticViews;
    this.subcircuitInstanceViews = subcircuitInstanceViews;
  }
}

