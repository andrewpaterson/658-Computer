package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferView;

import java.util.List;

public class BufferData
    extends StandardIntegratedCircuitData<BufferView, State>
{
  protected int bufferCount;

  public BufferData()
  {
  }

  public BufferData(Int2D position,
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
  public BufferView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new BufferView(circuitEditor,
                          position,
                          rotation,
                          new BufferProperties(name,
                                               FamilyStore.getInstance().get(family),
                                               explicitPowerPorts,
                                               bufferCount));
  }
}

