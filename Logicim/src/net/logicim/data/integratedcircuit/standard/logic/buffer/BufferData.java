package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.SimulationState;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
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
                    SimulationIntegratedCircuitEventData events,
                    List<SimulationMultiPortData> ports,
                    boolean selected,
                    SimulationState<State> simulationState,
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
          simulationState,
          explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }

  @Override
  public BufferView create(SubcircuitEditor subcircuitEditor, CircuitSimulation simulation, TraceLoader traceLoader, boolean fullLoad)
  {
    return new BufferView(subcircuitEditor.getSubcircuitView(),
                          simulation,
                          position,
                          rotation,
                          new BufferProperties(name,
                                               FamilyStore.getInstance().get(family),
                                               explicitPowerPorts,
                                               inputCount,
                                               inputWidth));
  }
}

