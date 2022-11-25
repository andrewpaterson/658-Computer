package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.or.NorGateView;

import java.util.List;

public class NorGateData
    extends LogicGateData<NorGateView>
{
  public NorGateData(Int2DData position,
                     RotationData rotation,
                     List<IntegratedCircuitEventData<?>> events,
                     List<PortData> portData,
                     int inputCount)
  {
    super(position, rotation, events, portData, inputCount);
  }

  @Override
  public NorGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new NorGateView(circuitEditor,
                           inputCount,
                           position.toInt2D(),
                           rotation.toRotation());
  }
}

