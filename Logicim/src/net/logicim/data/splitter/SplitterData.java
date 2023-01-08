package net.logicim.data.splitter;

import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.integratedcircuit.standard.bus.SplitterProperties;
import net.logicim.ui.integratedcircuit.standard.bus.SplitterView;

public class SplitterData
    extends PassiveData
{
  protected int outputCount;
  protected int outputOffset;
  protected int spacing;

  @Override
  protected ComponentView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new SplitterView(circuitEditor,
                            position,
                            rotation,
                            new SplitterProperties(name,
                                                   outputCount,
                                                   outputOffset,
                                                   spacing));
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
  }
}

