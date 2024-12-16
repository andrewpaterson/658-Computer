package net.logicim.data.integratedcircuit.common;

import net.common.type.Int2D;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.simulation.CircuitLoaders;

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
                     Set<Long> simulations,
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
    this.simulation = simulations;
  }

  @Override
  public void createAndConnectComponentDuringLoad(ViewPath viewPath,
                                                  CircuitSimulation circuitSimulation,
                                                  CircuitLoaders circuitLoaders,
                                                  PASSIVE passive)
  {
    passive.createComponent(viewPath, circuitSimulation);

    loadPorts(viewPath,
              circuitSimulation,
              circuitLoaders,
              passive);
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulation.contains(id);
  }
}

