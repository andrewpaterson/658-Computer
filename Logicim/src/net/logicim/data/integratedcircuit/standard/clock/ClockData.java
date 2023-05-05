package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;

import java.util.List;

public class ClockData
    extends StandardIntegratedCircuitData<ClockView, ClockOscillatorState>
{
  protected float frequency;
  protected boolean inverseOut;

  public ClockData()
  {
  }

  public ClockData(Int2D position,
                   Rotation rotation,
                   String name,
                   Family family,
                   float frequency,
                   SimulationIntegratedCircuitEventData events,
                   List<SimulationMultiPortData> ports,
                   long id,
                   boolean enabled,
                   boolean selected,
                   SimulationStateData<ClockOscillatorState> simulationState,
                   boolean inverseOut,
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
    this.frequency = frequency;
    this.inverseOut = inverseOut;
  }

  @Override
  public ClockView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new ClockView(subcircuitEditor.getSubcircuitView(),
                         position,
                         rotation,
                         new ClockProperties(name,
                                             FamilyStore.getInstance().get(family),
                                             explicitPowerPorts,
                                             frequency,
                                             inverseOut));
  }
}

