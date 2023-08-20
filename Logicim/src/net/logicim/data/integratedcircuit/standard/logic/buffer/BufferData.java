package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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
                    long id,
                    boolean enabled,
                    boolean selected,
                    SimulationStateData<State> simulationState,
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
          id,
          enabled,
          selected,
          simulationState,
          explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }

  @Override
  public BufferView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new BufferView(subcircuitEditor.getCircuitSubcircuitView(),
                          position,
                          rotation,
                          new BufferProperties(name,
                                               FamilyStore.getInstance().get(family),
                                               explicitPowerPorts,
                                               inputCount,
                                               inputWidth));
  }
}

