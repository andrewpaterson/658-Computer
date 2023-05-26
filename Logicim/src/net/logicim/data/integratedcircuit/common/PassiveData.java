package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.simulation.SubcircuitEditor;

import java.util.List;
import java.util.Set;

public abstract class PassiveData<PASSIVE extends PassiveView<?, ?>>
    extends ComponentData<PASSIVE>
{
  public Set<Long> simulation;

  public PassiveData()
  {
  }

  public PassiveData(Int2D position,
                     Rotation rotation,
                     String name,
                     Set<Long> simulationIDs,
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
    simulation = simulationIDs;
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

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulation.contains(id);
  }
}

