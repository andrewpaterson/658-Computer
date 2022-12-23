package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.LogicPortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.BufferView;

import java.util.List;

public class BufferData
    extends StandardIntegratedCircuitData<BufferView, State>
{
  public BufferData()
  {
  }

  public BufferData(Int2D position,
                    Rotation rotation,
                    String name,
                    String family,
                    List<IntegratedCircuitEventData<?>> events,
                    List<LogicPortData> logicPortData,
                    State state,
                    boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          logicPortData,
          state,
          explicitPowerPorts);
  }

  @Override
  public BufferView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new BufferView(circuitEditor,
                          position,
                          rotation,
                          name,
                          FamilyStore.getInstance().get(family),
                          explicitPowerPorts);
  }
}

