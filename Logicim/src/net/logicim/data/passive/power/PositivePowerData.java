package net.logicim.data.passive.power;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.passive.power.PositivePowerView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;
import java.util.Set;

public class PositivePowerData
    extends PassiveData<PositivePowerView>
{
  private float voltage;

  public PositivePowerData()
  {
  }

  public PositivePowerData(Int2D position,
                           Rotation rotation,
                           String name,
                           Set<Long> simulations,
                           List<SimulationMultiPortData> ports,
                           long id,
                           boolean enabled,
                           boolean selected,
                           float voltage)
  {
    super(position,
          rotation,
          name,
          simulations,
          ports,
          id,
          enabled,
          selected);
    this.voltage = voltage;
  }

  @Override
  protected PositivePowerView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new PositivePowerView(subcircuitEditor.getCircuitSubcircuitView(),
                                 position,
                                 rotation,
                                 new PositivePowerProperties(name, voltage));
  }
}

