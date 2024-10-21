package net.logicim.data.passive.power;

import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.passive.power.GroundView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.List;
import java.util.Set;

public class GroundData
    extends PassiveData<GroundView>
{
  public GroundData()
  {
  }

  public GroundData(Int2D position,
                    Rotation rotation,
                    String name,
                    Set<Long> simulations,
                    List<SimulationMultiPortData> ports,
                    long id,
                    boolean enabled,
                    boolean selected)
  {
    super(position,
          rotation,
          name,
          simulations,
          ports,
          id,
          enabled,
          selected);
  }

  protected GroundView createComponentView(SubcircuitEditor subcircuitEditor)
  {
    return new GroundView(subcircuitEditor.getInstanceSubcircuitView(),
                          position,
                          rotation,
                          new GroundProperties(name));
  }
}

