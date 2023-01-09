package net.logicim.data.splitter;

import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.integratedcircuit.standard.bus.SplitterProperties;
import net.logicim.ui.integratedcircuit.standard.bus.SplitterView;

public class SplitterData
    extends PassiveData<SplitterView>
{
  protected int outputCount;
  protected int outputOffset;
  protected int spacing;

  @Override
  protected SplitterView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new SplitterView(circuitEditor,
                            position,
                            rotation,
                            new SplitterProperties(name,
                                                   outputCount,
                                                   outputOffset,
                                                   spacing));
  }
}

