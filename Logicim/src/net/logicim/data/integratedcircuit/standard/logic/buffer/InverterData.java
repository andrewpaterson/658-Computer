package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.InverterView;

import java.util.List;

public class InverterData
    extends IntegratedCircuitData<InverterView>
{
  public InverterData(Int2D position,
                      Rotation rotation,
                      List<IntegratedCircuitEventData<?>> events,
                      List<PortData> portData)
  {
    super(position, rotation, events, portData);
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    InverterView inverterView = new InverterView(circuitEditor, position, rotation);
    circuitEditor.createConnectionViews(inverterView);
    loadEvents(circuitEditor, inverterView);
    loadPorts(circuitEditor, traceLoader, inverterView);
    return inverterView;
  }
}

