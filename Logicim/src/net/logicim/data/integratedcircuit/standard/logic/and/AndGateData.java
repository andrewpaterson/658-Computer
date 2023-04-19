package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.IntData;
import net.logicim.data.common.LongData;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateProperties;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.AndGateView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AndGateData
    extends LogicGateData<AndGateView>
{
  public Map<Long, Integer> map;

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
          inputCount,
          inputWidth,
          explicitPowerPorts);
    map = new LinkedHashMap<>();
    map.put(3L, 4);
    map.put(6L, 7);
  }

  @Override
  public AndGateView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new AndGateView(subcircuitEditor.getSubcircuitView(),
                           position,
                           rotation,
                           new LogicGateProperties(name,
                                                   FamilyStore.getInstance().get(family),
                                                   explicitPowerPorts,
                                                   inputCount,
                                                   inputWidth));
  }
}

