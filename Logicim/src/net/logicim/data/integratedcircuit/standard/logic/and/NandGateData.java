package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.SimulationState;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateView;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;

import java.util.List;

public class NandGateData
    extends LogicGateData<NandGateView>
{
  public NandGateData()
  {
  }

  public NandGateData(Int2D position,
                      Rotation rotation,
                      String name,
                      Family family,
                      SimulationIntegratedCircuitEventData simulationEvents,
                      List<SimulationMultiPortData> ports,
                      long id,
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
          simulationEvents,
          ports,
          id,
          selected,
          simulationState,
          inputCount,
          inputWidth,
          explicitPowerPorts);
  }

  @Override
  public NandGateView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new NandGateView(subcircuitEditor.getSubcircuitView(),
                            position,
                            rotation,
                            new LogicGateProperties(name,
                                                    FamilyStore.getInstance().get(family),
                                                    explicitPowerPorts,
                                                    inputCount,
                                                    inputWidth));
  }
}

