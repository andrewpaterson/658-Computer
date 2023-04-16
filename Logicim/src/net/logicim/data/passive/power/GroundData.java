package net.logicim.data.passive.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.passive.power.GroundView;

import java.util.List;

public class GroundData
    extends PassiveData<GroundView>
{
  public GroundData()
  {
  }

  public GroundData(Int2D position,
                    Rotation rotation,
                    String name,
                    List<SimulationMultiPortData> ports,
                    long id,
                    boolean selected)
  {
    super(position,
          rotation,
          name,
          ports,
          id,
          selected);
  }

  protected GroundView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    return new GroundView(subcircuitEditor.getSubcircuitView(),
                          position,
                          rotation,
                          new GroundProperties(name));
  }
}

