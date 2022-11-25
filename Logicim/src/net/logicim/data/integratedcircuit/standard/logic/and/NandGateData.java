package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.and.NandGateView;

import java.util.List;

public class NandGateData
    extends LogicGateData<NandGateView>
{
  public NandGateData(Int2D position,
                      Rotation rotation,
                      List<IntegratedCircuitEventData<?>> events,
                      List<PortData> portData,
                      int inputCount)
  {
    super(position, rotation, events, portData, inputCount);
  }

  @Override
  public NandGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new NandGateView(circuitEditor,
                            inputCount,
                            position,
                            rotation);
  }
}

