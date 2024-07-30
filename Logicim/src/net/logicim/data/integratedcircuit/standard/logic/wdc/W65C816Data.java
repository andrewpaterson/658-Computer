package net.logicim.data.integratedcircuit.standard.logic.wdc;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.wdc.W65C816Properties;
import net.logicim.ui.simulation.component.integratedcircuit.wdc.W65C816View;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class W65C816Data
    extends StandardIntegratedCircuitData<W65C816View, W65C816State>
{
  public W65C816Data()
  {
  }

  public W65C816Data(Int2D position,
                     Rotation rotation,
                     String name,
                     Family family,
                     SimulationIntegratedCircuitEventData events,
                     List<SimulationMultiPortData> ports,
                     long id,
                     boolean enabled,
                     boolean selected,
                     SimulationStateData<W65C816State> state,
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
          state,
          explicitPowerPorts);
  }

  @Override
  protected W65C816View createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new W65C816View(subcircuitEditor.getCircuitSubcircuitView(),
                           position,
                           rotation,
                           new W65C816Properties(name,
                                                 FamilyStore.getInstance().get(family),
                                                 explicitPowerPorts));
  }
}

