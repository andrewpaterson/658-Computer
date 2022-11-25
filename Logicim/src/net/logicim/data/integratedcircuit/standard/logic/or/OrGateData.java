package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.integratedcircuit.standard.logic.or.OrGateView;

import java.util.List;

public class OrGateData
    extends LogicGateData<OrGateView>
{
  public OrGateData(Int2DData position,
                    RotationData rotation,
                    List<IntegratedCircuitEventData<?>> events,
                    List<PortData> portData,
                    int inputCount)
  {
    super(position, rotation, events, portData, inputCount);
  }

  @Override
  public OrGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new OrGateView(circuitEditor,
                          inputCount,
                          position.toInt2D(),
                          rotation.toRotation());
  }
}

