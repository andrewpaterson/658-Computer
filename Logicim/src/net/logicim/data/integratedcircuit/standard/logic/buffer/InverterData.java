package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.InverterView;

import java.util.List;

public class InverterData
    extends IntegratedCircuitData<InverterView, State>
{
  public InverterData()
  {
  }

  public InverterData(Int2D position,
                      Rotation rotation,
                      String name,
                      String family,
                      List<IntegratedCircuitEventData<?>> events,
                      List<PortData> portData,
                      State state)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          portData,
          state);
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new InverterView(circuitEditor,
                            position,
                            rotation,
                            name,
                            FamilyStore.getInstance().get(family));
  }
}

