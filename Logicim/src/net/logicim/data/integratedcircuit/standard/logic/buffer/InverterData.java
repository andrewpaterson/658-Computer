package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.InverterView;

import java.util.List;

public class InverterData
    extends StandardIntegratedCircuitData<InverterView, State>
{
  protected int inputWidth;
  protected int inputCount;

  public InverterData()
  {
  }

  public InverterData(Int2D position,
                      Rotation rotation,
                      String name,
                      Family family,
                      SimulationIntegratedCircuitEventData events,
                      List<SimulationMultiPortData> ports,
                      long id,
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
          selected,
          simulationState,
          explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }

  @Override
  public InverterView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new InverterView(subcircuitEditor.getSubcircuitView(),
                            position,
                            rotation,
                            new BufferProperties(name,
                                                 FamilyStore.getInstance().get(family),
                                                 explicitPowerPorts,
                                                 inputCount,
                                                 inputWidth));
  }
}

