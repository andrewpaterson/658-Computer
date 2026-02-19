package net.logicim.data.integratedcircuit.standard.flop.dtype;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.standard.flop.dtype.DTypeFlipFlopState;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.flop.dtype.DTypeFlipFlopProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.flop.dtype.DTypeFlipFlopView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class DTypeFlipFlopData
    extends StandardIntegratedCircuitData<DTypeFlipFlopView, DTypeFlipFlopState>
{
  public boolean inverseOut;
  public boolean setReset;

  public DTypeFlipFlopData()
  {
  }

  public DTypeFlipFlopData(Int2D position,
                           Rotation rotation,
                           String name,
                           Family family,
                           SimulationIntegratedCircuitEventData events,
                           List<SimulationMultiPortData> ports,
                           long id,
                           boolean enabled,
                           boolean selected,
                           SimulationStateData<DTypeFlipFlopState> simulationState,
                           boolean explicitPowerPorts,
                           boolean inverseOut,
                           boolean setReset)
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
    this.inverseOut = inverseOut;
    this.setReset = setReset;
  }

  @Override
  public DTypeFlipFlopView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new DTypeFlipFlopView(subcircuitEditor.getInstanceSubcircuitView(),
                                 position,
                                 rotation,
                                 new DTypeFlipFlopProperties(name,
                                                             FamilyStore.getInstance().get(family),
                                                             explicitPowerPorts,
                                                             inverseOut,
                                                             setReset));
  }
}

