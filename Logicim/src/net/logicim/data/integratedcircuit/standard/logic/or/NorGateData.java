package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.or.NorGateView;

import java.util.List;

public class NorGateData
    extends LogicGateData<NorGateView>
{
  public NorGateData()
  {
  }

  public NorGateData(Int2D position,
                     Rotation rotation,
                     String name,
                     List<IntegratedCircuitEventData<?>> events,
                     List<PortData> portData,
                     State state,
                     int inputCount)
  {
    super(position,
          rotation,
          name,
          events,
          portData,
          state,
          inputCount);
  }

  @Override
  public NorGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new NorGateView(circuitEditor,
                           inputCount,
                           position,
                           rotation,
                           name);
  }
}

