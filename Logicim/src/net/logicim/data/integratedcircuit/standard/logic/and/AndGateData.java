package net.logicim.data.integratedcircuit.standard.logic.and;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.AndGateView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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
          inputCount,
          inputWidth,
          explicitPowerPorts);
  }

  @Override
  public AndGateView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new AndGateView(subcircuitEditor.getInstanceSubcircuitView(),
                           position,
                           rotation,
                           new LogicGateProperties(name,
                                                   FamilyStore.getInstance().get(family),
                                                   explicitPowerPorts,
                                                   inputCount,
                                                   inputWidth));
  }
}

