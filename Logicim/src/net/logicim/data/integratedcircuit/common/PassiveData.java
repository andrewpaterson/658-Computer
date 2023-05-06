package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.simulation.SubcircuitEditor;

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
                     boolean enabled,
                     boolean selected)
  {
    super(position,
          rotation,
          name,
          ports,
          id,
          enabled,
          selected);
  }

  @Override
  public void createAndConnectComponent(SubcircuitEditor subcircuitEditor,
                                        CircuitSimulation simulation,
                                        TraceLoader traceLoader,
                                        PASSIVE passiveView)
  {
    passiveView.createComponent(simulation);

    loadPorts(simulation, traceLoader, passiveView);
  }
}

