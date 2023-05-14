package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public abstract class DecorativeData<T extends DecorativeView<?>>
    extends StaticData<T>
{
  public DecorativeData()
  {
  }

  public DecorativeData(String name,
                        Int2D position,
                        Rotation rotation,
                        long id,
                        boolean enabled,
                        boolean selected)
  {
    super(name,
          position,
          rotation,
          id,
          enabled,
          selected);
  }

  public void createAndConnectComponent(SubcircuitEditor subcircuitEditor,
                                        CircuitSimulation simulation,
                                        TraceLoader traceLoader,
                                        T componentView)
  {
  }
}

