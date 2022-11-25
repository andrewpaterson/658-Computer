package net.logicim.data.integratedcircuit.standard.logic.xor;

import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XorGateView;

import java.util.List;

public class XorGateData
    extends LogicGateData<XorGateView>
{
  public XorGateData(Int2DData position,
                     RotationData rotation,
                     List<IntegratedCircuitEventData<?>> events,
                     List<PortData> ports,
                     int inputCount)
  {
    super(position, rotation, events, ports, inputCount);
  }

  @Override
  public XorGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new XorGateView(circuitEditor,
                           inputCount,
                           position.toInt2D(),
                           rotation.toRotation());
  }
}

