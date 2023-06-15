package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class PassiveView<PASSIVE extends Passive, PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
{
  protected Map<InstanceCircuitSimulation, PASSIVE> simulationPassives;

  public PassiveView(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     PROPERTIES properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.simulationPassives = new LinkedHashMap<>();
  }

  @Override
  public PASSIVE createComponent(InstanceCircuitSimulation instanceCircuitSimulation)
  {
    validateCanCreateComponent(instanceCircuitSimulation);

    PASSIVE passive = createPassive(instanceCircuitSimulation);
    simulationPassives.put(instanceCircuitSimulation, passive);

    postCreateComponent(instanceCircuitSimulation, passive);
    return passive;
  }

  @Override
  protected void removeComponent(InstanceCircuitSimulation circuit)
  {
    simulationPassives.remove(circuit);
  }

  @Override
  protected void finaliseView()
  {
    createPortViews();
    super.finaliseView();
    subcircuitView.addPassiveView(this);
  }

  @Override
  public String getComponentType()
  {
    for (PASSIVE passive : simulationPassives.values())
    {
      return passive.getType();
    }
    return "";
  }

  public PASSIVE getComponent(InstanceCircuitSimulation circuit)
  {
    return simulationPassives.get(circuit);
  }

  protected Set<Long> saveSimulations()
  {
    LinkedHashSet<Long> simulationIDs = new LinkedHashSet<>();
    for (InstanceCircuitSimulation circuit : simulationPassives.keySet())
    {
      simulationIDs.add(circuit.getId());
    }
    return simulationIDs;
  }

  public abstract PassiveData<?> save(boolean selected);

  protected abstract PASSIVE createPassive(InstanceCircuitSimulation circuit);
}

