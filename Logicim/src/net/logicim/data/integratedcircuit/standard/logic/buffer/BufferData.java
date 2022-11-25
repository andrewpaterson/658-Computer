package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.BufferView;

import java.util.List;

public class BufferData
    extends IntegratedCircuitData<BufferView>
{
  public BufferData(Int2DData position,
                    RotationData rotation,
                    List<IntegratedCircuitEventData<?>> events,
                    List<PortData> portData)
  {
    super(position, rotation, events, portData);
  }

  @Override
  public BufferView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new BufferView(circuitEditor,
                          position.toInt2D(),
                          rotation.toRotation());
  }
}

