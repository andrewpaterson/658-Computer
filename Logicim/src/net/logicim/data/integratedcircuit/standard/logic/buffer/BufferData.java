package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferView;

import java.util.List;

public class BufferData
    extends StandardIntegratedCircuitData<BufferView, State>
{
  protected int inputCount;
  protected int inputWidth;

  public BufferData()
  {
  }

  public BufferData(Int2D position,
                    Rotation rotation,
                    String name,
                    Family family,
                    List<IntegratedCircuitEventData<?>> events,
                    List<MultiPortData> ports,
                    boolean selected,
                    State state,
                    int inputCount,
                    int inputWidth,
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
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }

  @Override
  public BufferView create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader)
  {
    return new BufferView(subcircuitView,
                          circuit,
                          position,
                          rotation,
                          new BufferProperties(name,
                                               FamilyStore.getInstance().get(family),
                                               explicitPowerPorts,
                                               inputCount,
                                               inputWidth));
  }
}

