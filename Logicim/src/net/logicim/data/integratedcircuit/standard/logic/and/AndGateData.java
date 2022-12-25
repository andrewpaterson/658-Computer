package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.LogicPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.and.AndGateView;

import java.util.List;

public class AndGateData
    extends LogicGateData<AndGateView>
{
  public AndGateData()
  {
  }

  public AndGateData(Int2D position,
                     Rotation rotation,
                     String name,
                     String family,
                     List<IntegratedCircuitEventData<?>> events,
                     List<PortData> ports,
                     State state,
                     int inputCount,
                     boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          state,
          inputCount,
          explicitPowerPorts);
  }

  public AndGateView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new AndGateView(circuitEditor,
                           inputCount,
                           position,
                           rotation,
                           name,
                           FamilyStore.getInstance().get(family),
                           explicitPowerPorts);
  }
}

