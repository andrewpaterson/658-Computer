package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
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
                   List<IntegratedCircuitEventData<?>> events,
                   List<SimulationMultiPortData> ports,
                   boolean selected,
                   ClockOscillatorState state,
                   boolean inverseOut,
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
    this.frequency = frequency;
    this.inverseOut = inverseOut;
  }

  @Override
  public ClockView create(SubcircuitEditor subcircuitEditor, CircuitSimulation simulation, TraceLoader traceLoader, boolean fullLoad)
  {
    return new ClockView(subcircuitEditor.getSubcircuitView(),
                         simulation,
                         position,
                         rotation,
                         new ClockProperties(name,
                                             FamilyStore.getInstance().get(family),
                                             explicitPowerPorts,
                                             frequency,
                                             inverseOut));
  }
}

