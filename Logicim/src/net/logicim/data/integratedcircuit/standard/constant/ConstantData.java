package net.logicim.data.integratedcircuit.standard.constant;

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
import net.logicim.ui.simulation.component.integratedcircuit.standard.constant.ConstantProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.constant.ConstantView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;

public class ConstantData
    extends StandardIntegratedCircuitData<ConstantView, State>
{
  protected int inputWidth;
  protected Radix radix;

  public ConstantData()
  {
  }

  public ConstantData(Int2D position,
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
  protected ConstantView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new ConstantView(subcircuitEditor.getInstanceSubcircuitView(),
                         position,
                         rotation,
                         new ConstantProperties(name,
                                                FamilyStore.getInstance().get(family),
                                                inputWidth,
                                                radix));
  }
}

