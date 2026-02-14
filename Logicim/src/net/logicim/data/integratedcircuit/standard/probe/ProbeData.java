package net.logicim.data.integratedcircuit.standard.probe;

import net.common.type.Int2D;
import net.logicim.data.common.Radix;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.probe.ProbeProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.probe.ProbeView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class ProbeData
    extends StandardIntegratedCircuitData<ProbeView, State>
{
  protected int inputWidth;
  protected Radix radix;

  public ProbeData()
  {
  }

  public ProbeData(Int2D position,
                   Rotation rotation,
                   String name,
                   Family family,
                   SimulationIntegratedCircuitEventData events,
                   List<SimulationMultiPortData> ports,
                   long id,
                   boolean enabled,
                   boolean selected,
                   SimulationStateData<State> state,
                   boolean explicitPowerPorts,
                   int inputWidth,
                   Radix radix)
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
          state,
          explicitPowerPorts);
    this.inputWidth = inputWidth;
    this.radix = radix;
  }

  @Override
  protected ProbeView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new ProbeView(subcircuitEditor.getInstanceSubcircuitView(),
                         position,
                         rotation,
                         new ProbeProperties(name,
                                             FamilyStore.getInstance().get(family),
                                             inputWidth,
                                             radix));
  }
}

