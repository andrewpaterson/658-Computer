package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.InverterView;

import java.util.List;

public class InverterData
    extends StandardIntegratedCircuitData<InverterView, State>
{
  protected int bufferCount;

  public InverterData()
  {
  }

  public InverterData(Int2D position,
                      Rotation rotation,
                      String name,
                      String family,
                      List<IntegratedCircuitEventData<?>> events,
                      List<MultiPortData> ports,
                      boolean selected,
                      State state,
                      int bufferCount,
                      boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          selected,
          state,
          explicitPowerPorts);
    this.bufferCount = bufferCount;
  }

  @Override
  public InverterView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new InverterView(circuitEditor,
                            position,
                            rotation,
                            new BufferProperties(name,
                                                 FamilyStore.getInstance().get(family),
                                                 explicitPowerPorts,
                                                 bufferCount));
  }
}

