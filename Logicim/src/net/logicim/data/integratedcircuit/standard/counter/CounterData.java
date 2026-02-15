package net.logicim.data.integratedcircuit.standard.counter;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.standard.counter.CounterState;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.counter.CounterProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.counter.CounterView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class CounterData
    extends StandardIntegratedCircuitData<CounterView, CounterState>
{
  public int bitWidth;
  public int terminalValue;

  public CounterData()
  {
  }

  public CounterData(Int2D position,
                     Rotation rotation,
                     String name,
                     Family family,
                     SimulationIntegratedCircuitEventData events,
                     List<SimulationMultiPortData> ports,
                     long id,
                     boolean enabled,
                     boolean selected,
                     SimulationStateData<CounterState> simulationState,
                     boolean explicitPowerPorts,
                     int bitWidth,
                     int terminalValue)
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
    this.bitWidth = bitWidth;
    this.terminalValue = terminalValue;
  }

  @Override
  public CounterView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new CounterView(subcircuitEditor.getInstanceSubcircuitView(),
                           position,
                           rotation,
                           new CounterProperties(name,
                                                 FamilyStore.getInstance().get(family),
                                                 explicitPowerPorts,
                                                 bitWidth,
                                                 terminalValue));
  }
}

