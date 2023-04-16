package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;

import java.util.List;

public abstract class PassiveData<PASSIVE extends PassiveView<?, ?>>
    extends ComponentData<PASSIVE>
{
  public PassiveData()
  {
  }

  public PassiveData(Int2D position,
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
}

