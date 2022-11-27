package net.logicim.data.integratedcircuit.standard.logic.xor;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XnorGateView;

import java.util.List;

public class XnorGateData
    extends LogicGateData<XnorGateView>
{
  public XnorGateData()
  {
  }

  public XnorGateData(Int2D position,
                      Rotation rotation,
                      String name,
                      List<IntegratedCircuitEventData<?>> events,
                      List<PortData> ports,
                      int inputCount)
  {
    super(position,
          rotation,
          name,
          events,
          ports,
          inputCount);
  }

  @Override
  public XnorGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new XnorGateView(circuitEditor,
                            inputCount,
                            position,
                            rotation,
                            name);
  }
}

