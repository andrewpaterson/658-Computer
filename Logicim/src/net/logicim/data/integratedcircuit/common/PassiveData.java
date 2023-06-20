package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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
                                        SubcircuitSimulation circuit,
                                        CircuitLoaders circuitLoaders,
                                        PASSIVE passive)
  {
    passive.createComponent(circuit);

    loadPorts(circuit, circuitLoaders, passive);
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulation.contains(id);
  }
}

